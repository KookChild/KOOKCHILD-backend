package com.service.kookchild.domain.mission.domain;


import com.service.kookchild.global.domain.BaseEntity;
import com.service.kookchild.domain.user.domain.ParentChild;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_child_id")
    private ParentChild parentChild;
    private String title;
    private String content;
    private String reward;
    private String image;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean parentConfirm;
    private boolean childConfirm;
}
