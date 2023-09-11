package com.service.kookchild.domain.mission.domain;


import com.service.kookchild.domain.mission.dto.MissionUpdateDTO;
import com.service.kookchild.global.domain.BaseEntity;
import com.service.kookchild.domain.user.domain.ParentChild;
import lombok.*;

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

    @Builder
    public Mission(ParentChild parentChild, String title, String content, String reward, String image, LocalDateTime startDate,
                   LocalDateTime endDate){
        this.parentChild = parentChild;
        this.title = title;
        this.content = content;
        this.reward = reward;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setMission(MissionUpdateDTO missionUpdateDTO) {
        this.title = missionUpdateDTO.getTitle();
        this.content = missionUpdateDTO.getContent();
        this.reward = missionUpdateDTO.getReward();
        this.endDate = missionUpdateDTO.getEndDate().atStartOfDay();
    }

    public void requestConfirm(boolean childConfirm){
        this.childConfirm = childConfirm;
    }
}
