package com.service.kookchild.domain.quiz.dto;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private Long id;
    private String title;
    private int level;
    private int totalReward;
    private boolean isCorrect;

    public static QuizDTO of(QuizState qs) {
        Quiz q = qs.getQuiz();
        return QuizDTO.builder()
                .id(q.getId())
                .title(q.getTitle())
                .level(q.getLevel())
                .totalReward(qs.getTotalReward())
                .isCorrect(qs.isCorrect()).build();
    }
}
