package com.service.kookchild.domain.management.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FindAccountUpdateDTO {
    //DB 리턴 전용 DTO
    private Long balance;
    private String accountNum;
}
