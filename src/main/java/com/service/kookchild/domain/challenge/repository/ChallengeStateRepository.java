package com.service.kookchild.domain.challenge.repository;
import com.service.kookchild.domain.challenge.domain.ChallengeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeStateRepository extends JpaRepository<ChallengeState,Long> {
    @Modifying
    @Query(value = "UPDATE challenge_state  SET  child_confirm = 1 WHERE challenge_id = :challengeId AND parent_child_id = :parentChildId", nativeQuery = true)
    void updateChildConfirm(Long challengeId, Long parentChildId);

    /* 2. (자녀가) 챌린지  0) 참여하기 1) 대기중 2) 승인하기 3) 진행중 */
    @Query(value="SELECT * from challenge_state cs JOIN parent_child p ON cs.parent_child_id = p.id WHERE p.child_id = :childId AND cs.challenge_id = :challengeId ", nativeQuery = true)
    ChallengeState getChallengeType(Long challengeId, Long childId);

    @Modifying
    @Query(value = "UPDATE challenge_state SET parent_confirm = 1, parent_reward = :parentReward WHERE id = :id ", nativeQuery = true)
    void updateParentConfirm(Long id, int parentReward);


    ChallengeState findByParentChildIdAndChallengeId(Long parentChildId, Long challengeId);

    List<ChallengeState> findByParentChildId(Long parentChildId);

    List<ChallengeState> findByParentChildIdIn(List<Long> parentChildId);




}
