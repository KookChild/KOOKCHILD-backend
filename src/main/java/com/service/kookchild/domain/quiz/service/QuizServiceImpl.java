package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import com.service.kookchild.domain.quiz.dto.QuizAnswerDTO;
import com.service.kookchild.domain.quiz.dto.QuizResultDTO;
import com.service.kookchild.domain.quiz.dto.TodayQuizDTO;
import com.service.kookchild.domain.quiz.dto.TodayQuizDetailDTO;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{

        private final QuizRepository quizRepository;
        private final QuizStateRepository quizStateRepository;
        private final UserRepository userRepository;
        private final ParentChildRepository parentChildRepository;

    @Override
    @Transactional
    public TodayQuizDTO getTodayQuiz(String email) {
        User child = findUser(email);
        ParentChild ps = parentChildRepository.findByChild(child);

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        QuizState todayQuizState = quizStateRepository.findByCreatedDateBetweenAndParentChild(startOfDay, endOfDay, ps);
        if (todayQuizState == null) {
            List<Long> solvedQuizIds = quizStateRepository.findQuizIdsByParentChild(ps);

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
        TodayQuizDTO todayQuizDTO = TodayQuizDTO.builder()
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

    private User findUser(String email){
            return userRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("존재하지 않은 유저입니다.")
            );
        }

    }
