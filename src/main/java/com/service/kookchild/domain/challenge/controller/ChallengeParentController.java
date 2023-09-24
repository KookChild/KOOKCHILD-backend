package com.service.kookchild.domain.challenge.controller;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.dto.ChallengeParentConfirmDTO;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChallengeParentController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ParentChildRepository parentChildRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    ChallengeStateService challengeStateService;

    @Autowired
    ChallengeStateRepository challengeStateRepository;

    /* 부모가 챌린지에 대한 confirm을 업데이트 */
    @PostMapping("/challenge/detail/{challenge_id}/parentConfirm")
    public ResponseEntity updateParentConfirm(Authentication authentication, @PathVariable Long challenge_id, @RequestBody ChallengeParentConfirmDTO challengeParentConfirmDTO) {
        ParentChild parentChild = parentChildRepository.findByChildId(challengeParentConfirmDTO.getChildId()).get();
        ChallengeState challengeState = null;
        try {
            challengeState = challengeStateRepository.findByParentChildIdAndChallengeId(parentChild.getId(), challenge_id);
            if (challengeState != null) {
                challengeStateService.updateParentConfirm(challengeState.getId(), challengeParentConfirmDTO.getParentReward());
            } else {
                challengeStateService.createNewChallengeState(challenge_id, challengeParentConfirmDTO.getChildId(), challengeParentConfirmDTO.getParentReward());
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/challenge/parent")
    public ResponseEntity getChallengeList(Authentication authentication, @RequestParam Long child, @RequestParam(value = "type", defaultValue = "all") String type) {
        List<ChallengeState> challengeStates = null;
        String email = getEmail(authentication);
        User user = userRepository.findByEmail(email).get();
        List<Long> parentChildIds = new ArrayList<>();
        ;
        try {
            if (child == 0) { //0인 경우에는
                //전체보기
                List<ParentChild> parentChilds = parentChildRepository.findByParent(user);
                parentChildIds = parentChilds.stream()
                        .map(ParentChild::getId)
                        .collect(Collectors.toList());
                if ("all".equals(type)) {
                    List<Challenge> challenges = challengeRepository.getAllChallengesByParentChildIdIn(parentChildIds);
                    return new ResponseEntity(challenges, HttpStatus.OK);
                } else {
                    challengeStates = challengeStateService.getChallengeList(parentChildIds, type);
                    return new ResponseEntity(challengeStates, HttpStatus.OK);
                }
            }
            //실제 자녀 클릭 시

            ParentChild parentChild = parentChildRepository.findByChildId(child).get();
            parentChildIds.add(parentChild.getId());

            if ("all".equals(type)) {
                List<Challenge> challenges = challengeRepository.getAllChallengesByParentChildIdIn(parentChildIds);
                return new ResponseEntity(challenges, HttpStatus.OK);
            } else {
                challengeStates = challengeStateService.getChallengeList(parentChildIds, type);
                return new ResponseEntity(challengeStates, HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getEmail();
    }
}
