package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountResponse {

    private Account account;
    private List<AccountHistory> accountHistories;

    public static FindAccountResponse of(Account account, List<AccountHistory> accountHistories){
        return FindAccountResponse.builder()
                .account(account)
                .accountHistories(accountHistories)
                .build();
    }
}
