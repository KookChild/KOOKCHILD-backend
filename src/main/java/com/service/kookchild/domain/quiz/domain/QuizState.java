package com.service.kookchild.domain.quiz.domain;

import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.global.domain.BaseEntity;
import lombok.*;

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
    private boolean isSolved;

    private int totalReward;

    @Builder
    public QuizState(Quiz quiz, ParentChild parentChild, int totalReward){
        this.quiz = quiz;
        this.parentChild = parentChild;
        this.totalReward = totalReward;
    }

    public void updateIsCorrect(boolean isCorrect){
        this.isCorrect = isCorrect;
    }
    public void updateIsSolved(boolean isSolved){
        this.isSolved = isSolved;
    }
}
