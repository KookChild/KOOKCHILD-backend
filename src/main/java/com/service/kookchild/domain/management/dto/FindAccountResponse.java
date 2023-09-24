package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FindAccountResponse {

    private Long id;
    private String accountName;
    private Long balance;
    private AccountType type;
    private String accountNum;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static FindAccountResponse from(Account account){
        return FindAccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .balance(account.getBalance())
                .type(account.getType())
                .accountNum(account.getAccountNum())
                .createdDate(account.getCreatedDate())
                .modifiedDate(account.getModifiedDate())
                .build();
    }
}
