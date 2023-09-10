package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChallengeStateServiceImpl implements ChallengeStateService {

    @Autowired
    private ChallengeStateRepository challengeStateRepository;

    @Override
    public void updateParentConfirm(Long challengeId, ParentChild parentChild, int parentReward) {
        challengeStateRepository.updateParentConfirm(challengeId, parentChild.getId(), parentReward);

    }

    @Override
    @Transactional
    public void updateChildConfirm(Long challengeId, ParentChild parentChild) {
        Optional<ChallengeState> challengeStateOpt = challengeStateRepository.findByChallengeIdAndParentChildId(challengeId, parentChild.getId());

        if (challengeStateOpt.isPresent()) {
            ChallengeState challengeState = challengeStateOpt.get();
            challengeState.setChildConfirm(true);  // 또는 challengeState.childConfirm = true;
            challengeStateRepository.save(challengeState);
        } else {
            // 예외 처리
        }
    }

    // ChallengeStateServiceImpl.java

    public void updateProceedingStatus(Long challengeId, ParentChild parentChild) {
        Optional<ChallengeState> challengeStateOpt = challengeStateRepository.findByChallengeIdAndParentChildId(challengeId, parentChild.getId());
        challengeStateOpt.ifPresent(cs -> {
            if (cs.isParentConfirm() && cs.isChildConfirm()) {
                cs.setProceeding(true);
            }
        });
    }
}