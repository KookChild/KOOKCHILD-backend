package com.service.kookchild.domain.quiz.dto;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private Long id;
    private String title;
    private String answer;
    private int level;
    private int totalReward;
    private boolean isCorrect;
    private boolean isSolved;
    private LocalDateTime solvedDate;

    public static QuizDTO of(QuizState qs) {
        Quiz q = qs.getQuiz();
        return QuizDTO.builder()
                .id(q.getId())
                .title(q.getTitle())
                .answer(q.getAnswer())
                .level(q.getLevel())
                .totalReward(qs.getTotalReward())
                .isCorrect(qs.isCorrect())
                .solvedDate(qs.getModifiedDate()).build();
    }
}
