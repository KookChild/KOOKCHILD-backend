package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.quiz.dto.QuizAnswerDTO;
import com.service.kookchild.domain.quiz.dto.QuizResultDTO;
import com.service.kookchild.domain.quiz.dto.TodayQuizDTO;
import com.service.kookchild.domain.quiz.dto.TodayQuizDetailDTO;

public interface QuizService {
    TodayQuizDTO getTodayQuiz(String email);

    TodayQuizDetailDTO getTodayQuizDetail(String email, long quizId);

    QuizResultDTO checkQuizAnswer(String email, QuizAnswerDTO quizAnswerDTO);
}
