package com.service.kookchild.domain.management.domain;

import com.service.kookchild.global.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private boolean isDeposit;
    private Long amount;
    private String targetName;
    private String category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
