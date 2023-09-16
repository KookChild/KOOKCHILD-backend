package com.service.kookchild.domain.quiz.dto;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import com.service.kookchild.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizChildDTO {
    private String childName;
    private int totalReward;
    private boolean isCorrect;
    private String quizContent;

    public static QuizChildDTO of(QuizState qs) {
        Quiz q = qs.getQuiz();
        User child = qs.getParentChild().getChild();
        return QuizChildDTO.builder()
                .childName(child.getName())
                .totalReward(qs.getTotalReward())
                .isCorrect(qs.isCorrect())
                .quizContent(q.getContent()).build();
    }
}
