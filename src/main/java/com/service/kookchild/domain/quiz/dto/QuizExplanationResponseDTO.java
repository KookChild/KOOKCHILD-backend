package com.service.kookchild.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizExplanationResponseDTO {
    private String content;
    private int level;
    private String answer;
    private String explanation;
    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;
}
