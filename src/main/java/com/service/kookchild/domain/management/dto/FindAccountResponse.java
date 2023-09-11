package com.service.kookchild.domain.management.dto;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.user.domain.User;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountResponse {

    private Account account;
    private List<AccountHistory> accountHistories;


    public static FindAccountResponse of(Account account, List<AccountHistory> accountHistories) {
        return FindAccountResponse.builder()
                .account(account)
                .accountHistories(accountHistories)
                .build();
    }

    private Long id;
    private String accountName;
    private Long balance;
    private String password;
    private AccountType type;
    private String accountNum;
    private String userName;

    public FindAccountResponse(Long balance, String accountNum, String userName) {
        this.balance = balance;
        this.accountNum = accountNum;
        this.userName = userName;
    }
}
