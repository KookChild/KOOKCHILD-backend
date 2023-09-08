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
    private Long childId;

    private Long parentId;
}
