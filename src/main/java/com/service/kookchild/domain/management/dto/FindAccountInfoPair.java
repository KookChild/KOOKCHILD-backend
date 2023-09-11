package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountInfoPair {
    private String childId;
    private String parentId;

    public FindAccountInfoPair(String childId) {
        System.out.println("생성자 객체생성");
        this.childId = childId;
        this.parentId = null;
    }
}
