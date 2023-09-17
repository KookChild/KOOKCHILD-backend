package com.service.kookchild.domain.challenge.controller;

import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.dto.ChallengeParentConfirmDTO;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import com.service.kookchild.domain.challenge.service.ChallengeStateService;
import com.service.kookchild.domain.security.CustomUserDetails;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Controller
public class ChallengeParentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ParentChildRepository parentChildRepository;

    @Autowired
    ChallengeStateService challengeStateService;

    @Autowired
    ChallengeStateRepository challengeStateRepository;

    /* 부모가 챌린지에 대한 confirm을 업데이트 */
    @PostMapping("/challenge/detail/{challenge_id}/parentConfirm")
    public ResponseEntity updateParentConfirm(Authentication authentication, @PathVariable Long challenge_id, @RequestBody ChallengeParentConfirmDTO challengeParentConfirmDTO) {

        try {
            ParentChild parentChild = parentChildRepository.findByChildId(challengeParentConfirmDTO.getChildId()).get();
            ChallengeState challengeState = challengeStateRepository.findByParentChildIdAndChallengeId(parentChild.getId(), challenge_id);
            challengeStateService.updateParentConfirm(challengeState.getId(),challengeParentConfirmDTO.getParentReward() );
            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getEmail();
    }
}
