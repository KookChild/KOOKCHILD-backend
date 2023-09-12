package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
