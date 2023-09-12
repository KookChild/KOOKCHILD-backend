package com.service.kookchild.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionParentListDTO {
    private long missionAmount;
    private List<MissionViewDTO> missionLists;
    private List<ChildListDTO> childLists;
}
