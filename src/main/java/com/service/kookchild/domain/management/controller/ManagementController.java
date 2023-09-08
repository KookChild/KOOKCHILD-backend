package com.service.kookchild.domain.management.controller;

import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/info")
    public ResponseEntity<FindAccountResponse> findAccountInfo(){
        //비지니스 로직 실행해서
        //값을 가져왔어
        //그갑승ㄹ 보내주는거지
        FindAccountResponse findAccountResponse = new FindAccountResponse(1L,"김지은 통장",10000L,"1234", AccountType.예금);
        return ResponseEntity.ok(findAccountResponse);
    }
}
