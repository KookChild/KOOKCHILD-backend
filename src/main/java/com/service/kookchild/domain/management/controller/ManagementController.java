package com.service.kookchild.domain.management.controller;

import com.service.kookchild.domain.management.dto.CheckChildMoneyResponse;
import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountDTO;

import com.service.kookchild.domain.management.dto.FindAccountResponse;
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

    @GetMapping("/info")
    public ResponseEntity<FindAccountResponse> findAccountInfo(Authentication authentication){
        String email = getEmail(authentication);
        return ResponseEntity.ok(managementService.getAccountInfo(email));
    }

    @PostMapping("/send")
    public ResponseEntity sendMoney(Authentication authentication, @RequestBody FindAccountInfoPair fi){
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);

        fi.setParentId(String.valueOf(id));
        FindAccountDTO findAccountDTO = null;
        try {
            findAccountDTO = managementSendingService.sendChildMoney(fi);
        }catch(Exception e){

            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(findAccountDTO, HttpStatus.OK);
    }

    @GetMapping("/{child_id}")
    public ResponseEntity checkChildMoney (Authentication authentication, @PathVariable int child_id){
        System.out.println(child_id);

        CheckChildMoneyResponse checkChildMoneyResponse = null;
        try{
            System.out.println("컨트롤러 진입");
            checkChildMoneyResponse = managementSendingService.checkChildMoney(new FindAccountInfoPair(String.valueOf(child_id)));
        } catch (NumberFormatException e) {
            System.out.println("Invalid child_id format: " + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(checkChildMoneyResponse, HttpStatus.OK);
    }


    @GetMapping("/childName")
    public ResponseEntity getChildName(Authentication authentication){
        ArrayList list = null;
        String email = getEmail(authentication);
        Long id = managementSendingService.findUserId(email);
        try{
            list = managementSendingService.findChildNamesByParentId(id);
        }catch(Exception e){
            System.out.println(e);
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }
}