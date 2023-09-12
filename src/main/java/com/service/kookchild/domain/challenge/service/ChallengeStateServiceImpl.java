package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeStateServiceImpl implements ChallengeStateService {

    @Autowired
    private ChallengeStateRepository challengeStateRepository;

    @Override
    public void updateChildConfirm(Long challengeId, Long parentChildId) {
        System.out.println(challengeId);
        System.out.println(parentChildId);
        challengeStateRepository.updateChildConfirm(challengeId, parentChildId);

    }
}
