package com.service.kookchild.domain.quiz.domain;

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
public class QuizState extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "parentChild")
    private ParentChild parentChild;

    private boolean isCorrect;
}
