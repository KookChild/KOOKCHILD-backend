package com.service.kookchild.domain.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountChildNameId {
    //DB 리턴 전용 DTO
    private String name;
    private Long id;
}
