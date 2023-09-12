package com.service.kookchild.domain.management.dto;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CheckChildMoneyResponse {

    private String accountNum;
    private String userName;
    private Long amount;
    private Long notInAmount;

    public static CheckChildMoneyResponse of(String accountNum, String userName, Long amount, Long notInAmount){
        return CheckChildMoneyResponse.builder()
                .accountNum(accountNum)
                .userName(userName)
                .amount(amount)
                .notInAmount(notInAmount)
                .build();
    }
}
