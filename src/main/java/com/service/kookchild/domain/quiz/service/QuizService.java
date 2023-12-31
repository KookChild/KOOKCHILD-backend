package com.service.kookchild.domain.quiz.service;

import com.service.kookchild.domain.quiz.dto.*;

public interface QuizService {
    QuizDTO getTodayQuiz(String email);

    TodayQuizDetailDTO getTodayQuizDetail(String email, long quizId);

    boolean checkQuizAnswer(String email, QuizAnswerDTO quizAnswerDTO);

    HistoryQuizListDTO getHistoryQuizList(String email, String search);

    QuizExplanationResponseDTO explainQuiz(Long quizId);

    QuizDetailDTO getHistoryQuizDetail(String email, long quizId);

    QuizParentListDTO getChildQuizList(String email, long child);

    QuizQuestionAnswerDTO askQuestion(String questiong);
}
