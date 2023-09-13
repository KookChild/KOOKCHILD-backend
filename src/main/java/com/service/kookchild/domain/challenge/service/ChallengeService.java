package com.service.kookchild.domain.challenge.service;

import com.service.kookchild.domain.challenge.domain.Challenge;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeService {
    List<Challenge> getAllChallenge(Long id);
    Challenge findChallengeById(Long id);
    List<Challenge> getChallengeListByChildId(Long childId);

    List<Challenge> getRecommendedChallenges(Long userId);





}