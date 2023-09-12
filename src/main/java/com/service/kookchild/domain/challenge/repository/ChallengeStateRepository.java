package com.service.kookchild.domain.challenge.repository;
import com.service.kookchild.domain.challenge.domain.ChallengeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeStateRepository extends JpaRepository<ChallengeState,Long> {

    @Query(value = "UPDATE challenge_state  SET  child_confirm = 1 WHERE challenge_id = :challengeId AND parent_child_id = :parentChildId", nativeQuery = true)
    void updateChildConfirm(Long challengeId, Long parentChildId);
}
