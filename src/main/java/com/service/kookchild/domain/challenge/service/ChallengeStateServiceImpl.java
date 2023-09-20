package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.dto.ChallengeDTO;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeStateServiceImpl implements ChallengeStateService {

    private final ChallengeStateRepository challengeStateRepository;
    private final ChallengeRepository challengeRepository;
    private final ParentChildRepository parentChildRepository;

    @Override
    @Transactional
    public void updateChildConfirm(Long challengeId, Long parentChildId) {
        challengeStateRepository.updateChildConfirm(challengeId, parentChildId);

    }

    @Override
    @Transactional
    public void updateParentConfirm(Long id, int parentReward) {
        challengeStateRepository.updateParentConfirm(id,parentReward);
    }

    /*(자녀가) 챌린지  0) 참여하기 1) 대기중 2) 추천 - 승인하기 3) 진행중 */
    @Override
    public int getChallengeType(Long challengeId, Long childId) {
        ChallengeState challengeState = challengeStateRepository.getChallengeType(challengeId,childId);
        if(challengeState!=null) {
            if(challengeState.isChildConfirm()&& !challengeState.isParentConfirm()){ // 대기중
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

    @Override
    public List<ChallengeState> getChallengeList(List<Long> parentChildId, String type) {

/* 만약 type 이 all 이면 parentChildId List 에 해당하는 getAllChallenge  */
        List<ChallengeState> result = null;
        List<ChallengeState> challengeStates = challengeStateRepository.findByParentChildIdIn(parentChildId);
//        List<Challenge> challenges = challengeRepository.getAllChallenge()
        for (ChallengeState state : challengeStates) {
            System.out.println("result: " + state.toString() );
        }

        final String finalType = type;

        result = challengeStates.stream()
                .map(challengeState -> {
                    if ("all".equals(finalType)) {
                        return challengeState;
                    } else if ("ongoing".equals(finalType) && challengeState.isChildConfirm() && challengeState.isParentConfirm()) {
                        return challengeState;
                    } else if ("recommend".equals(finalType) && !challengeState.isChildConfirm() && challengeState.isParentConfirm()) {
                        return challengeState;
                    } else if ("request".equals(finalType) && challengeState.isChildConfirm() && !challengeState.isParentConfirm()) {
                        return challengeState;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return result;

    }

    public Long createNewChallengeState(Long challengeId, Long childId, int parentReward){
        Challenge challenge = challengeRepository.findById(challengeId).get();
        ParentChild parentChild = parentChildRepository.findByChildId(childId).get();
        boolean final_childConfirm = !(parentReward>0); /* parentReward가 0이면 child UI */
        boolean final_parentConfirm = parentReward>0;

        ChallengeState challengeState = ChallengeState.builder()
                .childConfirm(final_childConfirm)
                .parentConfirm(final_parentConfirm)
                .isSuccess(false)
                .parentReward(parentReward)
                .challenge(challenge)
                .parentChild(parentChild)
                .build();

        challengeStateRepository.save(challengeState);
        return 1L;

    }

}
