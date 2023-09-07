package com.service.kookchild.domain.management.domain;


import com.service.kookchild.global.domain.BaseEntity;
import com.service.kookchild.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_history_id")
    private AccountHistory accountHistory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String accountName;
    private Long balance;
    private String password;
    private AccountType type;
}
