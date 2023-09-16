package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.quiz.dto.*;

public interface QuizService {
    QuizDTO getTodayQuiz(String email);

    TodayQuizDetailDTO getTodayQuizDetail(String email, long quizId);

    QuizResultDTO checkQuizAnswer(String email, QuizAnswerDTO quizAnswerDTO);

    HistoryQuizListDTO getHistoryQuizList(String email, String search);

    QuizExplanationResponseDTO explainQuiz(Long quizId);

    QuizDetailDTO getHistoryQuizDetail(String email, long quizId);
}
