package com.service.kookchild.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionRecommendDTO {
    private String missionTitle;
    private String missionContent;
}
