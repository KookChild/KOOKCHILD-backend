package com.service.kookchild.domain.mission.controller;

import com.service.kookchild.domain.mission.dto.*;
import com.service.kookchild.domain.mission.service.MissionChildService;
import com.service.kookchild.domain.mission.service.MissionService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionChildController {

    private final MissionChildService missionChildService;
    private final MissionService missionService;

    @GetMapping("")
    public ResponseEntity getMissionList(
            Authentication authentication,
            @RequestParam(value = "sort", defaultValue = "newest") String state){
        String email = getEmail(authentication);
        MissionChildListDTO missionChildListDTO = missionChildService.getMissionList(email, state);
        return ResponseEntity.ok(missionChildListDTO);
    }

    @GetMapping("/history")
    public ResponseEntity getMissionHistoryList(
            Authentication authentication,
            @RequestParam(value = "sort", defaultValue = "newest") String state){
        String email = getEmail(authentication);
        MissionHistoryDTO missionHistoryDTO = missionChildService.getMissionHistoryList(email, state);
        return ResponseEntity.ok(missionHistoryDTO);
    }

    @PostMapping("")
    public void registerMission(@RequestBody MissionCreateDTO m, Authentication authentication){
        String email = getEmail(authentication);
        missionService.saveMission(m,email);
    }

    @GetMapping("/parent")
    public ResponseEntity getParentMissionList(
            Authentication authentication,
            @RequestParam Long child,
            @RequestParam(value = "type", defaultValue = "ongoing") String type){
        String email = getEmail(authentication);
        MissionParentListDTO missionParentListDTO = missionChildService.getParentMissionList(email, child, type);
        return ResponseEntity.ok(missionParentListDTO);
    }


    @GetMapping("/{missionId}")
    public ResponseEntity getMission(Authentication authentication, @PathVariable long missionId){
        String email = getEmail(authentication);
        MissionDetailDTO missionDetailDTO = missionChildService.getMission(email, missionId);
        if(missionDetailDTO == null) return ResponseEntity.badRequest().body("잘못된 접근입니다.");
        return ResponseEntity.ok(missionDetailDTO);
    }

    @PostMapping("/complete")
    public ResponseEntity requestMissionApproval(Authentication authentication, @RequestBody MissionDTO missionDTO){
        String email = getEmail(authentication);
        boolean result = missionChildService.requestMissionConfirm(email, missionDTO.getMissionId());
        if (!result) return ResponseEntity.badRequest().body("이미 승인요청이 된 미션입니다.");
        return ResponseEntity.ok("승인 요청 보내기 성공");
    }

    @PostMapping("/confirm")
    public ResponseEntity confirmMissionApproval(Authentication authentication, @RequestBody MissionDTO missionDTO){
        String email = getEmail(authentication);
        int result = missionChildService.confirmMission(email, missionDTO.getMissionId());
        if(result==403) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("금지된 접근입니다.");
        if(result==400) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 성공인증된 미션입니다.");
        if(result==422) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("잔액이 부족합니다");
        return ResponseEntity.ok("자녀 미션 성공 확인");
    }

    @PutMapping("")
    public ResponseEntity updateMission(Authentication authentication, @RequestBody MissionUpdateDTO missionUpdateDTO){
        String email = getEmail(authentication);
        boolean result = missionChildService.updateMission(email, missionUpdateDTO);
        if(!result) return ResponseEntity.badRequest().body("잘못된 접근입니다.");
        return ResponseEntity.ok("미션이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("")
    public ResponseEntity deleteMission(Authentication authentication, @RequestBody MissionDTO missionDTO){
        String email = getEmail(authentication);
        boolean result = missionChildService.deleteMission(email, missionDTO);
        if(!result) return ResponseEntity.badRequest().body("잘못된 접근입니다.");
        return ResponseEntity.ok("미션이 성공적으로 삭제되었습니다.");
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
        return principal.getEmail();
    }
}
