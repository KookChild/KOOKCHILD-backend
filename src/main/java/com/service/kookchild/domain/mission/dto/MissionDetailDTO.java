package com.service.kookchild.domain.mission.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class MissionDetailDTO {
    private String childName;
    private String title;
    private String content;
    private String reward;
    private String image;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String completeDate;
    private boolean parentConfirm;
    private boolean childConfirm;
    private boolean isParent;
    private String Imgurl;
}
