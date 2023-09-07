package com.service.kookchild.domain.management.controller;

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
        return ResponseEntity.ok(new FindAccountResponse());
    }
}
