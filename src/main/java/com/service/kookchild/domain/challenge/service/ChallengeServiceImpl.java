
package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.dto.ChallengeDTO;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ChallengeServiceImpl implements ChallengeService{

    @Autowired
    private ChallengeRepository challengeRepository;

    @Override
    public List<Challenge> getAllChallenge(Long parentChildId) {
        return challengeRepository.getAllChallenge(parentChildId);
    }

    @Override
    public Challenge findChallengeById(Long id) {
        return challengeRepository.findChallengeById(id);
    }


}