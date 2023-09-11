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

    public FindAccountInfoPair(String childId, String parentId) {
        System.out.println("생성자 객체생성");
        this.childId = childId;
        this.parentId = parentId;
    }

    public FindAccountInfoPair(String childId) {
        this.childId = childId;
    }
}
