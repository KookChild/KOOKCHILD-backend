package com.service.kookchild.domain.mission.dto;

import lombok.*;

import java.util.List;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionChildListDTO {

    private long missionAmount;
    private List<MissionViewDTO> missionLists;
}
