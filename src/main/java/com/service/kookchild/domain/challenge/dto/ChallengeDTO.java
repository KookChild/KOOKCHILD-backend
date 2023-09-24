package com.service.kookchild.domain.challenge.dto;

import com.service.kookchild.domain.challenge.domain.Challenge;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChallengeDTO {

    private Long id;
    private String title;
    private String childContent;
    private String parentContent;
    private String image;
    private int bankReward;
    LocalDateTime startDate;
    LocalDateTime endDate;
    private int parentReward;
}