package com.service.kookchild.domain.quiz.repository;

import com.service.kookchild.domain.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query(value = "SELECT * FROM (SELECT q.* FROM Quiz q WHERE q.id NOT IN (:solvedQuizIds) ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 1", nativeQuery = true)
    Quiz findRandomQuizExcluding(@Param("solvedQuizIds") List<Long> solvedQuizIds);

    @Query(value = "SELECT * FROM (SELECT q.* FROM Quiz q ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 1", nativeQuery = true)
    Quiz findRandomQuiz();


}
