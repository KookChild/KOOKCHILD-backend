package com.service.kookchild.domain.management.domain;


import com.service.kookchild.global.domain.BaseEntity;
import com.service.kookchild.domain.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String accountName;
    private Long balance;
    private String password;
    private AccountType type;
    @Column(unique = true)
    private String accountNum; //계좌번호

    @Builder
    public Account(User user, String accountName, long balance, String password, AccountType type, String accountNum){
        this.user = user;
        this.accountName = accountName;
        this.balance = balance;
        this.password = password;
        this.type = type;
        this.accountNum = accountNum;
    }
}
