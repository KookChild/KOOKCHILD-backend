package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.AccountType;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
public class FindAccountInfoPair {
    private String childId;
    private String parentId;
    private Long amount;

    public FindAccountInfoPair(String childId, String parentId, Long amount) {
        System.out.println("생성자 객체생성");
        this.childId = childId;
        this.parentId = parentId;
        this.amount = amount;
    }

    public FindAccountInfoPair(String childId){
        this.childId = childId;
    }

    public FindAccountInfoPair(String childId, Long amount) {

        this.childId = childId;
        this.amount = amount;
    }
}
