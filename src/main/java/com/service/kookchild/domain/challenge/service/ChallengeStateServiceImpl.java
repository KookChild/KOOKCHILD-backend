package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeStateServiceImpl implements ChallengeStateService {

    @Autowired
    private ChallengeStateRepository challengeStateRepository;

    @Override
    public void updateChildConfirm(Long challengeId, Long parentChildId) {
        challengeStateRepository.updateChildConfirm(challengeId, parentChildId);

    }
/*(자녀가) 챌린지  0) 참여하기 1) 대기중 2) 추천 - 승인하기 3) 진행중 */
    @Override
    public int getChallengeType(Long challengeId, Long childId) {
        ChallengeState challengeState = challengeStateRepository.getChallengeType(challengeId,childId);
        if(challengeState!=null) {
            if(challengeState.isChildConfirm() && !challengeState.isParentConfirm()){ // 대기중
                return 1;
            }
            if(!challengeState.isChildConfirm() && challengeState.isParentConfirm()){ //추천
                return 2;
            }
            if (challengeState.isChildConfirm() && challengeState.isParentConfirm()){ //진행중
                return 3;
            }

        }
        return 0; // 참여하기
    }
}
