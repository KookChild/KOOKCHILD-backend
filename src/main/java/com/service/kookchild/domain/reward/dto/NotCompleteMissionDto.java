package com.service.kookchild.domain.reward.dto;

import java.util.List;

import com.service.kookchild.domain.mission.dto.MissionTitleDateTimeDTO;
import com.service.kookchild.domain.mission.dto.MissionTitleDatesDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotCompleteMissionDto {

    private List<MissionTitleDateTimeDTO> missionContents;
    private String notCompleteMissionsAmount;

}
