package com.service.kookchild.domain.reward.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.dto.*;
import com.service.kookchild.domain.mission.exception.MissionNotFoundException;
import com.service.kookchild.domain.mission.repository.MissionChildRepository;
import com.service.kookchild.domain.mission.repository.MissionRepository;
import com.service.kookchild.domain.mission.service.MissionChildService;
import com.service.kookchild.domain.reward.dto.RewardInformationDTO;
import com.service.kookchild.domain.reward.repository.RewardRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final AccountHistoryRepository accountHistoryRepository;
    private final MissionRepository missionRepository;

    @Override
    public RewardInformationDTO rewardInformation(Long id) {

        return RewardInformationDTO.builder()
                .rewardCompleteAmount(accountHistoryRepository.rewardCompleteAmount(id))
                .rewardConfirmWaitAmount(missionRepository.rewardConfirmWaitAmount(id))
                .thisMonthInterestIncome(10L)
                .build();
    }
}

