package com.service.kookchild.domain.management.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FindAccountDTO {
    //DB 리턴 전용 DTO
    private String balance;
    private String accountNum;
    private String userName;
}
