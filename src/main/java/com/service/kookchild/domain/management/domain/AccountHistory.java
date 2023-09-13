package com.service.kookchild.domain.management.domain;

import com.service.kookchild.global.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account_history")
public class AccountHistory extends BaseEntity {


    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private int isDeposit;
    private Long amount;
    private String targetName;
    private String category;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public AccountHistory(Long userId, int isDeposit, Long amount, String targetName, String category, Account account) {
        this.userId = userId;
        this.isDeposit = isDeposit;
        this.amount = amount;
        this.targetName = targetName;
        this.category = category;
        this.account = account;
    }
}
