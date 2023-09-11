package com.service.kookchild.domain.mission.controller;

import com.service.kookchild.domain.mission.dto.MissionDetailDTO;
import com.service.kookchild.domain.mission.dto.MissionChildListDTO;
import com.service.kookchild.domain.mission.dto.MissionDTO;
import com.service.kookchild.domain.mission.dto.MissionRegisterDTO;
import com.service.kookchild.domain.mission.service.MissionChildService;
import com.service.kookchild.domain.mission.service.MissionService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mission")
public class MissionChildController {

    private final MissionChildService missionChildService;

    @GetMapping("")
    public ResponseEntity getMissionList(
            Authentication authentication,
            @RequestParam(value = "sort", defaultValue = "newest") String state){
        String email = getEmail(authentication);
        MissionChildListDTO missionChildListDTO = missionChildService.getMissionList(email, state);
        return ResponseEntity.ok(missionChildListDTO);
    }

    @GetMapping("/{missionId}")
    public ResponseEntity getMission(Authentication authentication, @PathVariable long missionId){
        String email = getEmail(authentication);
        MissionDetailDTO missionDetailDTO = missionChildService.getMission(email, missionId);
        return ResponseEntity.ok(missionDetailDTO);
    }

    @PostMapping("/complete")
    public ResponseEntity requestMissionApproval(Authentication authentication, @RequestBody MissionDTO missionDTO){
        String email = getEmail(authentication);
        missionChildService.requestMissionConfirm(email, missionDTO.getMissionId());
        return ResponseEntity.ok().build();
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
        return principal.getEmail();
    }
}
