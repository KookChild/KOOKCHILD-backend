package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import com.service.kookchild.domain.quiz.dto.*;
import com.service.kookchild.domain.quiz.repository.QuizRepository;
import com.service.kookchild.domain.quiz.repository.QuizStateRepository;
import com.service.kookchild.domain.security.ChatGptConfig;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{

        private final QuizRepository quizRepository;
        private final QuizStateRepository quizStateRepository;
        private final UserRepository userRepository;
        private final ParentChildRepository parentChildRepository;

    @Value("${CHATGPT-API-KEY}")
    private String apiKey;

    @Override
    @Transactional
    public QuizDTO getTodayQuiz(String email) {
        User child = findUser(email);
        ParentChild ps = parentChildRepository.findByChild(child);

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        QuizState todayQuizState = quizStateRepository.findByCreatedDateBetweenAndParentChild(startOfDay, endOfDay, ps);
        if (todayQuizState == null) {
            List<Long> solvedQuizIds = quizStateRepository.findQuizIdsByParentChildAndIsCorrect(ps);

            Quiz randomQuiz;
            if (solvedQuizIds.isEmpty()) {
                randomQuiz = quizRepository.findRandomQuiz();
            } else {
                randomQuiz = quizRepository.findRandomQuizExcluding(solvedQuizIds);
            }

            if (randomQuiz != null) {
                int totalReward = randomQuiz.getBankReward();
                if(randomQuiz.getLevel() == 1) totalReward += ps.getLevel1Reward();
                else if(randomQuiz.getLevel() == 2) totalReward += ps.getLevel2Reward();
                else totalReward += ps.getLevel3Reward();
                todayQuizState = QuizState.builder()
                        .quiz(randomQuiz)
                        .parentChild(ps)
                        .totalReward(totalReward)
                        .build();
                quizStateRepository.save(todayQuizState);
            }
        }
        QuizDTO todayQuizDTO = QuizDTO.builder()
                .id(todayQuizState.getQuiz().getId())
                .title(todayQuizState.getQuiz().getTitle())
                .level(todayQuizState.getQuiz().getLevel())
                .totalReward(todayQuizState.getTotalReward())
                .isCorrect(todayQuizState.isCorrect()).build();
        return todayQuizDTO;
    }

    @Override
    @Transactional
    public TodayQuizDetailDTO getTodayQuizDetail(String email, long quizId) {
        User child = findUser(email);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("해당 퀴즈가 존재하지 않습니다.")
        );
        ParentChild ps = parentChildRepository.findByChild(child);
        QuizState qs = quizStateRepository.findByQuizAndParentChild(quiz, ps);
        TodayQuizDetailDTO todayQuizDetailDTO = TodayQuizDetailDTO.builder()
                .title(quiz.getTitle())
                .content(quiz.getContent())
                .level(quiz.getLevel())
                .totalReward(qs.getTotalReward())
                .isCorrect(qs.isCorrect())
                .firstChoice(quiz.getFirstChoice())
                .secondChoice(quiz.getSecondChoice())
                .thirdChoice(quiz.getThirdChoice())
                .build();
        return todayQuizDetailDTO;
    }

    @Override
    @Transactional
    public QuizResultDTO checkQuizAnswer(String email, QuizAnswerDTO quizAnswerDTO) {
        User child = findUser(email);
        Quiz quiz = quizRepository.findById(quizAnswerDTO.getId()).orElseThrow(
                () -> new EntityNotFoundException("해당 퀴즈가 존재하지 않습니다.")
        );
        ParentChild pc = parentChildRepository.findByChild(child);
        QuizState qs = quizStateRepository.findByQuizAndParentChild(quiz, pc);
        if(quiz.getAnswer().equals(quizAnswerDTO.getAnswer())){
            qs.updateIsCorrect(true);
        }
        QuizResultDTO quizResultDTO = QuizResultDTO.builder()
                .isCorrect(qs.isCorrect()).build();
        return quizResultDTO;
    }

    @Override
    @Transactional
    public HistoryQuizListDTO getHistoryQuizList(String email) {
        User child = findUser(email);
        ParentChild pc = parentChildRepository.findByChild(child);
        List<QuizState> quizStateList = quizStateRepository.findByParentChildAndIsCorrect(pc, true);
        List<QuizDTO> quizListDTOList = quizStateList.stream()
                .map(QuizDTO::of)
                .collect(Collectors.toList());
        HistoryQuizListDTO historyQuizListDTO = HistoryQuizListDTO.builder()
                .historyQuizList(quizListDTOList).build();
        return historyQuizListDTO;
    }

    @Override
    public QuizExplanationResponseDTO explainQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("퀴즈가 존재하지 않습니다."));

        String content = quiz.getContent();
        String answer = quiz.getAnswer();
        String explanation = quiz.getExplanation();

        if(explanation==null) {

            String questionToGPT = content + "의 답은 " + answer + "입니다. 초등학생부터 고등학생까지의 학생들이 쉽게 이해할 수 있도록, 친근하면서도 공손한 평어체로 "+answer+"에 대해 3줄로 설명해주세요.";

            explanation = sendRequestToChatGPT(questionToGPT);

            quiz.updateExplanation(explanation);
            quizRepository.save(quiz);
        }

        QuizExplanationResponseDTO quizExplanationResponseDTO = QuizExplanationResponseDTO.builder()
                .title(quiz.getTitle())
                .content(content)
                .answer(answer)
                .explanation(explanation)
                .firstChoice(quiz.getFirstChoice())
                .secondChoice(quiz.getSecondChoice())
                .thirdChoice(quiz.getThirdChoice()).build();

        return quizExplanationResponseDTO;
    }

    @Override
    public QuizDetailDTO getHistoryQuizDetail(String email, long quizId) {
        ParentChild pc = parentChildRepository.findByChild(findUser(email));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("퀴즈가 존재하지 않습니다."));
        QuizState qs = quizStateRepository.findByQuizAndParentChild(quiz, pc);
        QuizDetailDTO quizDetailDTO = QuizDetailDTO.builder()
                .title(quiz.getTitle())
                .content(quiz.getContent())
                .answer(quiz.getAnswer())
                .explanation(quiz.getExplanation())
                .level(quiz.getLevel())
                .totalReward(qs.getTotalReward())
                .isCorrect(qs.isCorrect()).build();
        return quizDetailDTO;
    }

    private String sendRequestToChatGPT(String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        headers.setContentType(MediaType.valueOf(ChatGptConfig.MEDIA_TYPE));

        Map<String, Object> message = new HashMap<>();
        message.put("role", ChatGptConfig.ROLE);
        message.put("content", question);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", ChatGptConfig.MODEL);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", ChatGptConfig.MAX_TOKEN);
        requestBody.put("temperature", ChatGptConfig.TEMPERATURE);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(ChatGptConfig.URL, entity, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();

            List<?> choices = (List<?>) responseBody.get("choices");
            if (choices.isEmpty()) {
                throw new RuntimeException("Choices list is empty!");
            }

            Object choiceObject = choices.get(0);
            if (!(choiceObject instanceof Map)) {
                throw new RuntimeException("Expected a Map but found: " + choiceObject.getClass());
            }

            Map<String, Object> choiceMap = (Map<String, Object>) choiceObject;
            Map<String, Object> messageMap = (Map<String, Object>) choiceMap.get("message");
            String content = (String) messageMap.get("content");
            return content;
        } else {
            throw new RuntimeException("Failed to get response from OpenAI");
        }
    }


    private User findUser(String email){
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("존재하지 않은 유저입니다.")
            );
        }

    }
