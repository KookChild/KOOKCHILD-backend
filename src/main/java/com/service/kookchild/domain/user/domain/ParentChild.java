package com.service.kookchild.domain.user.domain;

import com.service.kookchild.global.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParentChild extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private User child;
    private int level1Reward;
    private int level2Reward;
    private int level3Reward;

    @Builder
    public ParentChild(User parent, User child, int level1Reward, int level2Reward, int level3Reward){
        this.parent = parent;
        this.child = child;
        this.level1Reward = level1Reward;
        this.level2Reward = level2Reward;
        this.level3Reward = level3Reward;
    }

}
