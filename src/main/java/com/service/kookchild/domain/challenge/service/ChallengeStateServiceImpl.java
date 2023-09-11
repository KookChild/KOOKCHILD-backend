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
        /*
        부모님의 추천, 자녀가 신청한 챌린지의 승인은 동일하게 해당 Confirm 로직으로 처리됨.
        */
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
        /*
        챌린지의 부모 확인, 자녀 확인이 모두 1일 시, 챌린지의 상태 "proceeding"을 true로 변경하는 메소드
        부모, 자녀의 Confirm 상태 변경 시 함께 실행 될 것.
         */
        Optional<ChallengeState> challengeStateOpt = challengeStateRepository.findByChallengeIdAndParentChildId(challengeId, parentChild.getId());
        challengeStateOpt.ifPresent(cs -> {
            if (cs.isParentConfirm() && cs.isChildConfirm()) {
                cs.setProceeding(true);
            }
        });
    }
}