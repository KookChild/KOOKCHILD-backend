package com.service.kookchild.domain.challenge.domain;

import com.service.kookchild.global.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Challenge extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String Title;
    private String childContent;
    private String parentContent;
    private String image;
    private int bankReward;
    LocalDateTime startDate;
    LocalDateTime endDate;
}