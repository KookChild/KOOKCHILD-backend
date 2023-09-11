package com.service.kookchild.domain.user.controller;

import com.service.kookchild.domain.user.dto.LoginRequestDTO;
import com.service.kookchild.domain.user.dto.LoginResponseDTO;
import com.service.kookchild.domain.user.dto.RegisterRequestDTO;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) throws Exception{
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterRequestDTO request) throws Exception{
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }
}
