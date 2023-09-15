package com.service.kookchild.domain.reward.service;


import com.service.kookchild.domain.reward.dto.NotCompleteMissionDto;
import com.service.kookchild.domain.reward.dto.RewardInformationDTO;
import com.service.kookchild.domain.user.domain.User;

public interface RewardService {
    RewardInformationDTO rewardInformation(Long id);
    NotCompleteMissionDto notCompleteMissions(Long id);

}
