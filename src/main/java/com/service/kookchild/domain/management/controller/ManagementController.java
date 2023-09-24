package com.service.kookchild.domain.management.controller;

import com.service.kookchild.domain.management.dto.*;

import com.service.kookchild.domain.management.service.AccountHistoryService;
import com.service.kookchild.domain.management.service.ManagementSendingService;
import com.service.kookchild.domain.management.service.ManagementService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {

    private final ManagementSendingService managementSendingService;
    private final ManagementService managementService;
    private final AccountHistoryService accountHistoryService;

    @GetMapping("/info")
    public ResponseEntity<FindAccountResponse> findAccountInfo(Authentication authentication){
        String email = getEmail(authentication);
        return ResponseEntity.ok(managementService.getAccountInfo(email));
    }

    @PostMapping("/send")
    public ResponseEntity sendMoney(Authentication authentication, @RequestBody FindAccountInfoPair fi){
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);

        fi.setParentId(id);
        FindAccountDTO findAccountDTO = null;
        try {
            findAccountDTO = managementSendingService.sendChildMoney(fi);
        }catch(Exception e){

            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(findAccountDTO, HttpStatus.OK);
    }


    @GetMapping("/childName")
    public ResponseEntity getChildName(Authentication authentication){
        FindAccountInformation list = null;
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);
        FindAccountInfoPair fi = new FindAccountInfoPair();
        fi.setParentId(id);
        try{
            list = managementSendingService.findChildNamesByParentId(fi);
        }catch(Exception e){
            System.out.println(e);
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<FindAccountHistoryResponse> findAccountHistory(Authentication authentication){
        FindAccountHistoryResponse accountHistories = accountHistoryService.findAccountHistories(authentication);

        return ResponseEntity.ok(accountHistories);
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }
}