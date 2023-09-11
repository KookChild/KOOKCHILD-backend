package com.service.kookchild.domain.mission.dto;

import com.service.kookchild.domain.mission.domain.Mission;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionChildViewDTO {
    private long id;
    private String title;
    private String reward;
    private String deadline;
    private boolean parentConfirm;

    public static MissionChildViewDTO of(Mission m) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        String deadline = m.getStartDate().format(outputFormatter) + " ~ " + m.getEndDate().format(outputFormatter);

        return MissionChildViewDTO.builder()
                .id(m.getId())
                .title(m.getTitle())
                .reward(m.getReward())
                .deadline(deadline)
                .parentConfirm(m.isParentConfirm())
                .build();
    }



}
