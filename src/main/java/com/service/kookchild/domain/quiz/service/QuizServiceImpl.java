package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.chatgpt.service.ChatGptService;
import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import com.service.kookchild.domain.quiz.dto.*;
import com.service.kookchild.domain.quiz.exception.InsufficientBalanceException;
import com.service.kookchild.domain.quiz.repository.QuizRepository;
import com.service.kookchild.domain.quiz.repository.QuizStateRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{

    private static final String CORRECT_ANSWER = "answer";

    private final QuizRepository quizRepository;
    private final QuizStateRepository quizStateRepository;
    private final UserRepository userRepository;
    private final ParentChildRepository parentChildRepository;
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final ChatGptService chatGptService;

    @Override
    @Transactional
    public QuizDTO getTodayQuiz(String email) {
        User child = findUser(email);
        ParentChild ps = parentChildRepository.findByChild(child);
        QuizState todayQuizState = findTodayQuiz(ps);

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
                .isCorrect(todayQuizState.isCorrect())
                .isSolved(todayQuizState.isSolved()).build();
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
        QuizState qs = findTodayQuiz(ps);
        TodayQuizDetailDTO todayQuizDetailDTO = TodayQuizDetailDTO.builder()
                .title(quiz.getTitle())
                .content(quiz.getContent())
                .answer(quiz.getAnswer())
                .level(quiz.getLevel())
                .totalReward(qs.getTotalReward())
                .firstChoice(quiz.getFirstChoice())
                .secondChoice(quiz.getSecondChoice())
                .thirdChoice(quiz.getThirdChoice())
                .build();
        return todayQuizDetailDTO;
    }

    @Override
    @Transactional
    public boolean checkQuizAnswer(String email, QuizAnswerDTO quizAnswerDTO) {
        User child = findUser(email);
        Quiz quiz = findQuiz(quizAnswerDTO.getId());
        ParentChild pc = findParentChildByChild(child);
        QuizState qs = findTodayQuiz(pc);

        boolean isCorrect = Optional.ofNullable(quizAnswerDTO.getAnswer()).orElse("").equals(CORRECT_ANSWER);
        qs.updateIsSolved(true);

        if (isCorrect) {
            qs.updateIsCorrect(true);
            handleReward(pc, child, qs.getTotalReward());
        }

        return isCorrect;
    }

    private ParentChild findParentChildByChild(User child) {
        return parentChildRepository.findByChild(child);
    }

    private Quiz findQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 퀴즈가 존재하지 않습니다."));
    }

    private void handleReward(ParentChild pc, User child, long quizReward) {
        User parent = pc.getParent();
        Account parentAccount = accountRepository.findAccountsByType1AndUserId(parent.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 계좌가 존재하지 않습니다."));

        if (quizReward <= parentAccount.getBalance()) {
            accountRepository.updateParentType1Balance(parent.getId(), quizReward);
            accountRepository.updateChildType2Balance(child.getId(), quizReward);
            createAccountHistory(child, quizReward);
        } else {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }
    }

    private void createAccountHistory(User child, long amount) {
        Account childAccount = accountRepository.findAccountByType2AndUserId(child.getId()).get();
        AccountHistory childHistory = AccountHistory.builder()
                .userId(child.getId())
                .isDeposit(1)
                .amount(amount)
                .targetName("부모")
                .category("리워드")
                .account(childAccount).build();
        accountHistoryRepository.save(childHistory);
    }



    @Override
    @Transactional
    public HistoryQuizListDTO getHistoryQuizList(String email, String search) {
        ParentChild pc = parentChildRepository.findByChild(findUser(email));

        List<QuizState> quizStateList;
        if (search != null && !search.trim().isEmpty()) {
            quizStateList = quizStateRepository.findByParentChildAndIsCorrectAndQuizAnswerContaining(pc, search);
        } else {
            quizStateList = quizStateRepository.findByParentChildAndIsCorrectOrderByModifiedDateDesc(pc, true);
        }

        List<QuizDTO> quizListDTOList = quizStateList.stream()
                .map(QuizDTO::of)
                .collect(Collectors.toList());
        HistoryQuizListDTO historyQuizListDTO = HistoryQuizListDTO.builder()
                .historyQuizList(quizListDTOList).build();
        return historyQuizListDTO;
    }


    @Override
    @Transactional
    public QuizExplanationResponseDTO explainQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("퀴즈가 존재하지 않습니다."));

        String content = quiz.getContent();
        String answer = quiz.getAnswer();
        String explanation = quiz.getExplanation();

        if(explanation==null) {

            String questionToGPT = content + "의 답은 " + answer + "입니다. 초등학생부터 고등학생까지의 학생들이 쉽게 이해할 수 있도록, 친근하면서도 공손한 대화체로 "+answer+"에 대해 3줄로 설명해주세요.";

            explanation = chatGptService.sendRequestToChatGPT(questionToGPT);

            quiz.updateExplanation(explanation);
            quizRepository.save(quiz);
        }

        QuizExplanationResponseDTO quizExplanationResponseDTO = QuizExplanationResponseDTO.builder()
                .content(content)
                .level(quiz.getLevel())
                .answer(answer)
                .explanation(explanation)
                .firstChoice(quiz.getFirstChoice())
                .secondChoice(quiz.getSecondChoice())
                .thirdChoice(quiz.getThirdChoice()).build();

        return quizExplanationResponseDTO;
    }

    @Override
    @Transactional
    public QuizQuestionAnswerDTO askQuestion(String question) {
        String subQuestion = " 초등학생부터 고등학생까지의 학생들이 쉽게 이해할 수 있도록, 친근하면서도 공손한 대화체로 3줄 이내로 간단하게 설명해주세요. 그리고 존댓말로 써주세요.";
        String questionToGPT = question + subQuestion;
        String answer = chatGptService.sendRequestToChatGPT(questionToGPT);
        QuizQuestionAnswerDTO quizQuestionAnswerDTO = QuizQuestionAnswerDTO.builder()
                .answer(answer).build();
        return quizQuestionAnswerDTO;
    }

    @Override
    @Transactional
    public QuizDetailDTO getHistoryQuizDetail(String email, long quizId) {
        ParentChild pc = parentChildRepository.findByChild(findUser(email));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("퀴즈가 존재하지 않습니다."));
        QuizState qs = quizStateRepository.findByQuizAndParentChildAndIsCorrect(quiz, pc, true);
        QuizDetailDTO quizDetailDTO = QuizDetailDTO.builder()
                .answer(quiz.getAnswer())
                .explanation(quiz.getExplanation())
                .level(quiz.getLevel()).build();
        return quizDetailDTO;
    }

    @Override
    @Transactional
    public QuizParentListDTO getChildQuizList(String email, long child) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<QuizState> quizStateList = new ArrayList<>();
        if(child==0) {
            List<ParentChild> childList = parentChildRepository.findByParent(findUser(email));
            List<Long> parentChildIds = childList.stream()
                    .map(ParentChild::getId)
                    .collect(Collectors.toList());
            quizStateList = quizStateRepository.findByCreatedDateBetweenAndIsSolvedAndParentChildIdIn(startOfDay, endOfDay, true, parentChildIds);
        }else{
            ParentChild parentChild = parentChildRepository.findByChildId(child).get();
            quizStateList = quizStateRepository.findByCreatedDateBetweenAndIsSolvedAndParentChildId(startOfDay, endOfDay, true, parentChild.getId());
        }
        List<QuizChildDTO> quizChildList = quizStateList.stream()
                .map(QuizChildDTO::of)
                .collect(Collectors.toList());
        QuizParentListDTO quizParentListDTO = QuizParentListDTO.builder()
                .childLists(quizChildList).build();
        return quizParentListDTO;
    }

    private QuizState findTodayQuiz(ParentChild ps) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return quizStateRepository.findByCreatedDateBetweenAndParentChild(startOfDay, endOfDay, ps);
    }

    private User findUser(String email){
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("존재하지 않은 유저입니다.")
            );
        }

    }
