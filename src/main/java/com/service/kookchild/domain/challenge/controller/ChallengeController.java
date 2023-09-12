package com.service.kookchild.domain.challenge.controller;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.service.ChallengeService;
import com.service.kookchild.domain.challenge.service.ChallengeStateService;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeStateService challengeStateService;

    /*(자녀가) 자신이 참여하고 있는 챌린지 및 부모에게 추천받은 챌린지 조회 */
    @GetMapping("/challenge")
    public ResponseEntity<List<Challenge>> select(@RequestParam(value = "state", defaultValue = "all") String state, HttpServletRequest request) {
        try {
            List<Challenge> challengeList = null;
            HttpSession session = request.getSession();

            switch (state) {

                case "all":
                    challengeList = challengeService.getAllChallenge();
                    break;
                case "proceeding":
                    /* 임시 :: 아직 로그인 로직 구현안됨 */
                    challengeList = challengeService.getChallengeListByChildId((Long) session.getAttribute("vo"));
                    break;
                case "parentConfirmed":
                    /* 임시 :: 아직 로그인 로직 구현안됨 */
                    challengeList = challengeService.getRecommendedChallenges((Long) session.getAttribute("vo"));
                    break;
                default:
                    challengeList = challengeService.getAllChallenge();
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
            HttpSession session = request.getSession();
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
                    challengeList = challengeService.getAllChallenge();
            }
            return new ResponseEntity(challengeList,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Challenge 상세내용 조회*/
    /* 나중에 프론트 측에서 child,parent를 param으로 구분해서 content type 구분해주기*/
    @GetMapping("/challenge/{challenge_id}")
    public ResponseEntity select( @PathVariable Long challenge_id){
        try{
            Challenge challenge =  (Challenge) challengeService.findChallengeById(challenge_id);
            return new ResponseEntity(challenge,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 부모가 챌린지에 대한 confirm을 업데이트 */
    @PutMapping("/challenge/{challenge_id}/parent_confirm")
    public ResponseEntity updateParentConfirm(@PathVariable Long challenge_id,
                                              @RequestParam ParentChild parentChild, // 적절한 방법으로 ParentChild 객체를 얻습니다.
                                              @RequestParam int parentReward) {
        try {
            challengeStateService.updateParentConfirm(challenge_id, parentChild, parentReward);
            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 자녀가 챌린지에 대한 confirm을 업데이트 */
    @PutMapping("/challenge/{challenge_id}/child_confirm")
    public ResponseEntity updateChildConfirm(@PathVariable Long challenge_id,
                                             @RequestParam ParentChild parentChild) { // 적절한 방법으로 ParentChild 객체를 얻습니다.
        try {
            challengeStateService.updateChildConfirm(challenge_id, parentChild);
            return new ResponseEntity(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}