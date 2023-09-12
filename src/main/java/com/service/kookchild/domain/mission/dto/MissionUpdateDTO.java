package com.service.kookchild.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionUpdateDTO {
    private long missionId;
    private String title;
    private String content;
    private String reward;
    private LocalDate endDate;
}