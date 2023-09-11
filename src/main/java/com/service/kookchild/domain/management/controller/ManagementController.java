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
            fr = managementSendingService.sendChildMoney(fi);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(fr, HttpStatus.OK);
    }

    @GetMapping("/{child_id}")
    public ResponseEntity checkChildMoney (@PathVariable int child_id){
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
}
