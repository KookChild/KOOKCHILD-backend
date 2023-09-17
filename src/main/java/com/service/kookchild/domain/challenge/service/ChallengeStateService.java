package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.ChallengeState;

public interface ChallengeStateService {

    void updateChildConfirm(Long challengeId, Long parentChildId);

    void updateParentConfirm( Long id, int parentReward);
    int getChallengeType(Long challengeId, Long childId);


}
