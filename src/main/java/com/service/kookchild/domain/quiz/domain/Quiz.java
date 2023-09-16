package com.service.kookchild.domain.quiz.domain;

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
public class Quiz extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String content;
    private String answer;
    @Column(name = "quiz_level")
    private int level;
    private int bankReward;
    @Lob
    private String explanation;
    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;

    public void updateExplanation(String explanation){
        this.explanation = explanation;
    }
}
