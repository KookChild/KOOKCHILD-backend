package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.transaction.annotation.Transactional;

public interface ChallengeStateService {

    @Transactional
    void updateParentConfirm(Long challengeId, ParentChild parentChild, int parentReward);


    @Transactional
    void updateChildConfirm(Long challengeId, ParentChild parentChild);

}
