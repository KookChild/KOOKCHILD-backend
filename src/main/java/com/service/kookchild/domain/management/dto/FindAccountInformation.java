package com.service.kookchild.domain.management.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountInformation {
    //DB 리턴 전용 DTO
   private Long balance;
   private ArrayList<FindAccountChildNameId> list;

}
