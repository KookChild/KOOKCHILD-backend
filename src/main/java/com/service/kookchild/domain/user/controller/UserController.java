package com.service.kookchild.domain.user.controller;

import com.service.kookchild.domain.security.CustomUserDetails;
import com.service.kookchild.domain.user.dto.*;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) throws Exception{
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public  ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request){
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }

    @GetMapping("/check-email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = userService.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/user")
    public ResponseEntity<FindUserResponseDTO> findUserInfo(Authentication authentication){
        log.info("/main/parent Controller 진입");
        String email = getEmail (authentication);
        FindUserResponseDTO parentMainInfo = userService.getUserInfo(email);
        log.info("/main/paren t Controller 반환");
        return ResponseEntity.ok(parentMainInfo);
    }

    private String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }

}
