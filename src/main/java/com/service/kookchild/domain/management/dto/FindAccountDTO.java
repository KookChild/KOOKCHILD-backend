package com.service.kookchild.domain.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountDTO {

    private Long balance;
    private String accountNum;
    private String userName;



}
