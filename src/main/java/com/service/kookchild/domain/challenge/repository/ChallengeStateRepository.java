package com.service.kookchild.domain.challenge.repository;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChallengeStateRepository extends JpaRepository<ChallengeState, Long> {
    Optional<ChallengeState> findByChallengeIdAndParentChildId(Long challengeId, Long parentChildId);


    @Query("UPDATE ChallengeState cs SET cs.parentConfirm = 1," +
            " cs.parentReward = :parentReward WHERE cs.challenge.id = :challengeId AND cs.parentChild.id = :parentChildId")
    void updateParentConfirm(@Param("challengeId") Long challengeId, @Param("parentChildId") Long parentChildId, @Param("parentReward") int parentReward);




}


