package com.service.kookchild.domain.finance.domain;

import com.service.kookchild.domain.management.domain.AccountType;
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
public class FinanceProduct extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String content;
    private float interest;

    @Enumerated(value = EnumType.STRING)
    private AccountType type;
}
