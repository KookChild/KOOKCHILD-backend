package com.service.kookchild.domain.challenge.controller;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.domain.ChallengeState;
import com.service.kookchild.domain.challenge.dto.ChallengeDTO;
import com.service.kookchild.domain.challenge.dto.ChallengeParentConfirmDTO;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import com.service.kookchild.domain.challenge.repository.ChallengeStateRepository;
import com.service.kookchild.domain.challenge.service.ChallengeService;
import com.service.kookchild.domain.challenge.service.ChallengeStateService;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.security.CustomUserDetails;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChallengeChildController {

    private final ChallengeService challengeService;

    private final ChallengeStateService challengeStateService;

    private final UserRepository userRepository;

    private final ParentChildRepository parentChildRepository;

    private final ChallengeRepository challengeRepository;

    private  final ChallengeStateRepository challengeStateRepository;

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
        ParentChild parentChild = parentChildRepository.findByChildId(user.getId()).get();
        try {
            List<Challenge> challengeList = null;
            List<ChallengeDTO> challengeDTOList = null;

            switch (state) {
                case "all":
                    challengeList = challengeService.getAllChallenge(parentChild.getId());
                    break;
                case "proceeding":
//                    challengeList = challengeService.getChallengeListByChildId(user.getId());
                    List<Object[]> proceedingResult = challengeRepository.getChallengeListByChildId(user.getId());
                    challengeDTOList = new ArrayList<>();
                    for (Object[] row : proceedingResult) {
                        ChallengeDTO dto = new ChallengeDTO();
                        dto.setId(((BigDecimal) row[0]).longValue());
                        dto.setTitle((String) row[1]);
                        dto.setChildContent((String) row[2]);
                        dto.setParentContent((String) row[3]);
                        dto.setImage((String) row[4]);
                        dto.setBankReward(((BigDecimal) row[5]).intValue());
                        dto.setStartDate(((Timestamp) row[6]).toLocalDateTime());
                        dto.setEndDate(((Timestamp) row[7]).toLocalDateTime());
                        dto.setParentReward(((BigDecimal) row[8]).intValue());
                        challengeDTOList.add(dto);
                    }
                    break;
                case "parentConfirmed":
//                    challengeList = challengeService.getRecommendedChallenges(user.getId());
                    List<Object[]> recommendResult = challengeRepository.getRecommendedChallenges(user.getId());
                    challengeDTOList = new ArrayList<>();
                    for (Object[] row : recommendResult) {
                        ChallengeDTO dto = new ChallengeDTO();
                        dto.setId(((BigDecimal) row[0]).longValue());
                        dto.setTitle((String) row[1]);
                        dto.setChildContent((String) row[2]);
                        dto.setParentContent((String) row[3]);
                        dto.setImage((String) row[4]);
                        dto.setBankReward(((BigDecimal) row[5]).intValue());
                        dto.setStartDate(((Timestamp) row[6]).toLocalDateTime());
                        dto.setEndDate(((Timestamp) row[7]).toLocalDateTime());
                        dto.setParentReward(((BigDecimal) row[8]).intValue());
                        challengeDTOList.add(dto);
                    }
                    break;
                default:
                    challengeList = challengeService.getAllChallenge(user.getId());
            }

            if (challengeDTOList != null) {
                return new ResponseEntity(challengeDTOList, HttpStatus.OK);
            } else if (challengeList != null) {
                return new ResponseEntity(challengeList, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* (부모가) 자녀별로 참여하고 있는 챌린지 및 추천한 챌린지 조회 */
    @GetMapping("/challenge/{child_id}")
    public ResponseEntity select( @PathVariable Long child_id,
                                 @RequestParam(value = "state", defaultValue = "parentConfirmed") String state){

        try{
            List<Challenge> challengeList = null;
            List<ChallengeDTO> challengeDTOList = null;
            switch (state) {
                /* (자녀가) 현재 참여중인 챌린지 */
                case "proceeding":
                    List<Object[]> proceedingResult = challengeRepository.getChallengeListByChildId(child_id);
                    challengeDTOList = new ArrayList<>();
                    for (Object[] row : proceedingResult) {
                        ChallengeDTO dto = new ChallengeDTO();
                        dto.setId(((BigDecimal) row[0]).longValue());
                        dto.setTitle((String) row[1]);
                        dto.setChildContent((String) row[2]);
                        dto.setParentContent((String) row[3]);
                        dto.setImage((String) row[4]);
                        dto.setBankReward(((BigDecimal) row[5]).intValue());
                        dto.setStartDate(((Timestamp) row[6]).toLocalDateTime());
                        dto.setEndDate(((Timestamp) row[7]).toLocalDateTime());
                        dto.setParentReward(((BigDecimal) row[8]).intValue());
                        challengeDTOList.add(dto);
                    }
                    break;
                    /* (자녀에게) 추천을 한 챌린지 */
                case "parentConfirmed":
//                    challengeList = challengeService.getRecommendedChallenges(child_id);
                    List<Object[]> result = challengeRepository.getRecommendedChallenges(child_id);
                    challengeDTOList = new ArrayList<>();
                    for (Object[] row : result) {
                        ChallengeDTO dto = new ChallengeDTO();
                        dto.setId(((BigDecimal) row[0]).longValue());
                        dto.setTitle((String) row[1]);
                        dto.setChildContent((String) row[2]);
                        dto.setParentContent((String) row[3]);
                        dto.setImage((String) row[4]);
                        dto.setBankReward(((BigDecimal) row[5]).intValue());
                        dto.setStartDate(((Timestamp) row[6]).toLocalDateTime());
                        dto.setEndDate(((Timestamp) row[7]).toLocalDateTime());
                        dto.setParentReward(((BigDecimal) row[8]).intValue());
                        challengeDTOList.add(dto);
                    }
                    break;

                default:
                    challengeList = challengeService.getAllChallenge(child_id);
            }
            if (challengeDTOList != null) {
                return new ResponseEntity(challengeDTOList, HttpStatus.OK);
            } else if (challengeList != null) {
                return new ResponseEntity(challengeList, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
@GetMapping(value={"/challenge/check/{challenge_id}/{child_id}","/challenge/check/{challenge_id}"})
public ResponseEntity select(
        @PathVariable Long challenge_id,
        @PathVariable(required = false) Long child_id,
        Authentication authentication
) {
    Long id=null;
    if(child_id == null){
        String email = getEmail(authentication);
        User user = userRepository.findByEmail(email).get();
        id = user.getId();
    }
    else{
        id = child_id;
    }
    int result = 0;
    try{
        result = challengeStateService.getChallengeType(challenge_id,id);
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
        ChallengeState challengeState = null;
        challengeState = challengeStateRepository.findByParentChildIdAndChallengeId(parentChild.getId(),challenge_id);
        if(challengeState !=null){
        challengeStateService.updateChildConfirm(challenge_id, parentChild.getId());}
        else{
            challengeStateService.createNewChallengeState(challenge_id,user.getId(),0);
        }

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
