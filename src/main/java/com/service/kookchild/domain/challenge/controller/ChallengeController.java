package com.service.kookchild.domain.challenge.controller;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import com.service.kookchild.domain.challenge.service.ChallengeService;
import com.service.kookchild.domain.challenge.service.ChallengeStateService;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.security.CustomUserDetails;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.global.exception.ExceptionStatus;
import com.service.kookchild.global.exception.KookChildException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeStateService challengeStateService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentChildRepository parentChildRepository;

    @RequestMapping(value = "/test/hello")
    @ResponseBody
    public String helloKookchild(Model model) {
        return "Hello Kookchild";
    }

    /*(자녀가) 자신이 참여하고 있는 챌린지 및 부모에게 추천받은 챌린지 조회 */
    @GetMapping("/challenge")
    public ResponseEntity<List<Challenge>> select(Authentication authentication, @RequestParam(value = "state", defaultValue = "all") String state) {
        String email = getEmail(authentication);
        User user = userRepository.findByEmail(email).get();
        try {
            List<Challenge> challengeList = null;

            switch (state) {

                case "all":
                    challengeList = challengeService.getAllChallenge(user.getId());
                    break;
                case "proceeding":
                    user = userRepository.findByEmail(email).get();

                    /* 임시 :: 아직 로그인 로직 구현안됨 */
                    challengeList = challengeService.getChallengeListByChildId(user.getId());
                    break;
                case "parentConfirmed":
                    user = userRepository.findByEmail(email).get();
                    /* 임시 :: 아직 로그인 로직 구현안됨 */
                    challengeList = challengeService.getRecommendedChallenges(user.getId());
                    break;
                default:
                    challengeList = challengeService.getAllChallenge(user.getId());
            }

            return new ResponseEntity<>(challengeList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* (부모가) 자녀별로 참여하고 있는 챌린지 및 추천한 챌린지 조회 */
    @GetMapping("/challenge/{child_id}")
    public ResponseEntity select(HttpServletRequest request, @PathVariable Long child_id,
                                 @RequestParam(value = "state", defaultValue = "parentConfirmed") String state){
        try{
            List<Challenge> challengeList = null;
            switch (state) {
                /* (자녀가) 현재 참여중인 챌린지 */
                case "proceeding":
                    challengeList = challengeService.getChallengeListByChildId(child_id);
                    /* (자녀에게) 추천을 한 챌린지 */
                case "parentConfirmed":
                    challengeList = challengeService.getRecommendedChallenges(child_id);
                    break;
                /* default: 전체 챌린지 목록? --논의필요 */
                default:
                    challengeList = challengeService.getAllChallenge(child_id);
            }
            return new ResponseEntity(challengeList,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Challenge 상세페이지 조회*/
    @GetMapping("/challenge/detail/{challenge_id}")
    public ResponseEntity select( @PathVariable Long challenge_id){
        try{
            Challenge challenge = challengeService.findChallengeById(challenge_id);
            return new ResponseEntity(challenge,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/* (자녀가) 챌린지  0) 참여하기 1) 대기중 2) 승인하기 3) 진행중  */
@GetMapping("/challenge/check/{challenge_id}")
public ResponseEntity select( @PathVariable Long challenge_id, Authentication authentication){
    String email = getEmail(authentication);
    User user = userRepository.findByEmail(email).get();
    int result = 0;
    try{
        result = challengeStateService.getChallengeType(challenge_id, user.getId());
        return new ResponseEntity(result,HttpStatus.OK);
    }
    catch(Exception e){
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    /* 자녀가 챌린지 참여요청 혹은 추천 챌린지 승인 */
@PostMapping ("/challenge/detail/{challenge_id}/childConfirm")
public ResponseEntity updateChildConfirm(Authentication authentication, @PathVariable  Long challenge_id) {
    String email = getEmail(authentication);
    User user = userRepository.findByEmail(email).get();
    ParentChild parentChild = parentChildRepository.findByChildId(user.getId()).get();

    try {
        challengeStateService.updateChildConfirm(challenge_id, parentChild.getId());
        return new ResponseEntity(HttpStatus.OK);
    } catch(Exception e) {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getEmail();
    }
    /* 부모가 챌린지에 대한 confirm을 업데이트 */
//    @PutMapping("/challenge/{challenge_id}/parent_confirm")
//    public ResponseEntity updateParentConfirm(@PathVariable Long challenge_id,
//                                              @RequestParam ParentChild parentChild, // 적절한 방법으로 ParentChild 객체를 얻습니다.
//                                              @RequestParam int parentReward) {
//        try {
//            challengeStateService.updateParentConfirm(challenge_id, parentChild, parentReward);
//            return new ResponseEntity(HttpStatus.OK);
//        } catch(Exception e) {
//            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return principal.getEmail();
//    }



}
