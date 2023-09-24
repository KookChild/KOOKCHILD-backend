package com.service.kookchild.domain.reward.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RewardInformationDTO {

    private String name;
    private String rewardCompleteAmount;
    private String rewardConfirmWaitAmount;
    private String thisMonthInterestIncome;

}
