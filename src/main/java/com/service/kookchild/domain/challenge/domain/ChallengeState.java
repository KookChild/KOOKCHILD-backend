package com.service.kookchild.domain.challenge.domain;

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
public class ChallengeState extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "parent_child_id")
    private ParentChild parentChild;
    private int parentReward;
    private boolean parentConfirm;
    private boolean childConfirm;
    private boolean isSuccess;


    public void setChildConfirm(boolean childConfirm) {
        this.childConfirm = childConfirm;
    }

}