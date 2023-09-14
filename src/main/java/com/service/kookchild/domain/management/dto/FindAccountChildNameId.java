package com.service.kookchild.domain.management.dto;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountChildNameId {
    //DB 리턴 전용 DTO
    private String name;
    private String accountNum;
    private String spendingAmount;
    private String savingAmount;
    private Long id;

    public FindAccountChildNameId(String name, String accountNum, Long id) {
        this.name = name;
        this.accountNum = accountNum;
        this.id = id;
    }
}
