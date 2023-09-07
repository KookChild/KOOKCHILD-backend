package com.service.kookchild.domain.finance.domain;


import com.service.kookchild.global.domain.BaseEntity;
import com.service.kookchild.domain.user.domain.ParentChild;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoreInterestRate extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "parent_child_id")
    private ParentChild parentChild;

    @ManyToOne
    @JoinColumn(name = "finance_product_id")
    private FinanceProduct financeProduct;

    @Enumerated(value = EnumType.STRING)
    private PeriodType periodType;

    private float parent_interest;
    private Long allowance;


}
