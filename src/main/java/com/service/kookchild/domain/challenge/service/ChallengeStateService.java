package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import java.util.List;

public interface ChallengeStateService {

    void updateChildConfirm(Long challengeId, Long parentChildId);

    void updateParentConfirm( Long id, int parentReward);
    int getChallengeType(Long challengeId, Long childId);

    List<ChallengeState> getChallengeList (List<Long> parentChildId, String type);

    Long createNewChallengeState(Long challengeId, Long childId, int parentReward);


}
