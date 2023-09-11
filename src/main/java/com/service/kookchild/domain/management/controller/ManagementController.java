package com.service.kookchild.domain.management.controller;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.service.ManagementSendingService;
import com.service.kookchild.domain.management.service.ManagementService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {

    private final ManagementSendingService managementSendingService;
    private final ManagementService managementService;

    @GetMapping("/info")
    public ResponseEntity<FindAccountResponse> findAccountInfo(@RequestBody Long accountId){
        return ResponseEntity.ok(managementService.getAccountInfo(accountId));
    }

    @PostMapping("/send")
    public ResponseEntity sendMoney(Authentication authentication, @RequestBody Long childId){
        String email = getEmail(authentication);
        FindAccountInfoPair fi = null;
        fi.setChildId(String.valueOf(childId));
        Long id = managementSendingService.findUserId(email);
        String name = managementSendingService.findUserNameById(childId);

        fi.setParentId(String.valueOf(id));

        FindAccountResponse fr = null;
        try {

            fr = managementSendingService.sendChildMoney(fi);
            fr.setUserName(name);

        }catch(Exception e){

            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(fr, HttpStatus.OK);
    }

    @GetMapping("/{child_id}")
    public ResponseEntity checkChildMoney (Authentication authentication, @PathVariable int child_id){
        System.out.println(child_id);

        FindAccountResponse fr = null;
        try{
            System.out.println("컨트롤러 진입");
            fr = managementSendingService.checkChildMoney(new FindAccountInfoPair(String.valueOf(child_id)));
        } catch (NumberFormatException e) {
            System.out.println("Invalid child_id format: " + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(fr, HttpStatus.OK);
    }

    @GetMapping("/amount/{child_id}")
    public ResponseEntity getChildConsumptionAndDeposit(Authentication authentication, @PathVariable int child_id){
        Long amount = null;
        try{
            amount = managementSendingService.FindConsumption(new FindAccountInfoPair(String.valueOf(child_id)));
        }catch(Exception e){
            System.out.println(e);
        }
        return new ResponseEntity(amount, HttpStatus.OK);
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }
}