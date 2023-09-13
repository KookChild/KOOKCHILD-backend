package com.service.kookchild.domain.mission.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionCreateDTO {

    private String title;
    private String content;
    private String reward;

    private String image;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdDate;
    private String childIds;
}
