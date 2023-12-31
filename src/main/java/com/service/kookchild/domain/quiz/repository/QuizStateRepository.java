package com.service.kookchild.domain.quiz.repository;

import com.service.kookchild.domain.quiz.domain.Quiz;
import com.service.kookchild.domain.quiz.domain.QuizState;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizStateRepository extends JpaRepository<QuizState, Long> {

    QuizState findByCreatedDateBetweenAndParentChild(LocalDateTime start, LocalDateTime end, ParentChild ps);

    @Query(value = "SELECT qs.quiz.id FROM QuizState qs WHERE qs.parentChild = :parentChild AND qs.isCorrect = true")
    List<Long> findQuizIdsByParentChildAndIsCorrect(@Param("parentChild") ParentChild parentChild);


    QuizState findByQuizAndParentChildAndIsCorrect(Quiz quiz, ParentChild ps, boolean isCorrect);

    List<QuizState> findByParentChildAndIsCorrectOrderByModifiedDateDesc(ParentChild pc, boolean isCorrect);

    @Query("SELECT qs FROM QuizState qs WHERE qs.parentChild = :parentChild AND qs.isCorrect = true AND qs.quiz.answer LIKE '%' || :search || '%' ORDER BY qs.modifiedDate DESC")
    List<QuizState> findByParentChildAndIsCorrectAndQuizAnswerContaining(@Param("parentChild") ParentChild parentChild, @Param("search") String search);

    List<QuizState> findByCreatedDateBetweenAndIsSolvedAndParentChildIdIn(LocalDateTime start, LocalDateTime end, boolean isSolved, List<Long> parentChildIds);
    List<QuizState> findByCreatedDateBetweenAndIsSolvedAndParentChildId(LocalDateTime start, LocalDateTime end, boolean isSolved, long parentChild);

}
