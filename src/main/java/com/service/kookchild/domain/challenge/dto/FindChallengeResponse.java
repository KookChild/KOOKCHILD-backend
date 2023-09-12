package com.service.kookchild.domain.challenge.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindChallengeResponse {
    private Long id;
    private String Title;
    private String childContent;
    private String parentContent;
    private String image;
    private int bankReward;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
