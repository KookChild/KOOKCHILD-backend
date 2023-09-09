package com.service.kookchild.domain.management.controller;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.service.ManagementSendingService;
import com.service.kookchild.domain.management.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {

    private final ManagementSendingService managementSendingService;

    @PostMapping("/send")
    public ResponseEntity sendMoney(@RequestBody FindAccountInfoPair fi){
        FindAccountResponse fr = null;
        try {
            fr = managementSendingService.sendMoney(fi);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(fr, HttpStatus.OK);
    }
}
