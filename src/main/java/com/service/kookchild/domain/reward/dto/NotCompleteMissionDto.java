package com.service.kookchild.domain.reward.dto;

import java.util.List;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotCompleteMissionDto {

    private List<String> missionContents;
    private String notCompleteMissionsAmount;

}
