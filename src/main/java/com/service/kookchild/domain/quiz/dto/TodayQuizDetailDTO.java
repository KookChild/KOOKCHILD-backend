package com.service.kookchild.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodayQuizDetailDTO {
    private String title;
    private String content;
    private String answer;
    private int level;
    private int totalReward;
    private boolean isCorrect;
    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;
}