
package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChallengeServiceImpl implements ChallengeService{

    @Autowired
    private ChallengeRepository challengeRepository;
    @Override
    public List<Challenge> getAllChallenge() {
        return challengeRepository.getAllChallenge();
    }

    @Override
    public Challenge findChallengeById(Long id) {
        return challengeRepository.findChallengeById(id);
    }

    @Override
    public List<Challenge> getChallengeListByChildId(Long childId) {
        return challengeRepository.getChallengeListByChildId(childId);
    }

    @Override
    public List<Challenge> getRecommendedChallenges(Long userId) {
        return challengeRepository.getRecommendedChallenges(userId);
    }
}