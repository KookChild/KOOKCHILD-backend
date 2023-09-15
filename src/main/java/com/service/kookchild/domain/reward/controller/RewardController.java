package com.service.kookchild.domain.reward.controller;

import com.service.kookchild.domain.management.dto.*;
import com.service.kookchild.domain.management.service.AccountHistoryService;
import com.service.kookchild.domain.management.service.ManagementSendingService;
import com.service.kookchild.domain.management.service.ManagementSendingServiceImpl;
import com.service.kookchild.domain.management.service.ManagementService;
import com.service.kookchild.domain.reward.dto.NotCompleteMissionDto;
import com.service.kookchild.domain.reward.dto.RewardInformationDTO;
import com.service.kookchild.domain.reward.service.RewardService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reward")
public class RewardController {

    private final RewardService rewardService;
    private final ManagementSendingService managementSendingService;

    @GetMapping("")
    public ResponseEntity<RewardInformationDTO> getRewardInformation(
            Authentication authentication
    ){
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);

        return ResponseEntity.ok(rewardService.rewardInformation(id));
    }

    @GetMapping("/notcomplete")
    public ResponseEntity<NotCompleteMissionDto> getNotCompleteMissions(
            Authentication authentication
    ){
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);

        return ResponseEntity.ok(rewardService.notCompleteMissions(id));
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }


}