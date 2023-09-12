package com.service.kookchild.domain.user.service;

import com.service.kookchild.domain.security.JwtProvider;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.dto.FindUserResponseDTO;
import com.service.kookchild.domain.user.dto.LoginRequestDTO;
import com.service.kookchild.domain.user.dto.LoginResponseDTO;
import com.service.kookchild.domain.user.dto.RegisterRequestDTO;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.global.exception.ExceptionStatus;
import com.service.kookchild.global.exception.KookChildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentChildRepository parentChildRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public boolean register(RegisterRequestDTO registerRequestDTO) throws Exception{
        try{
            User parent = insertUser(registerRequestDTO, true);
            userRepository.save(parent);
            List<RegisterRequestDTO> childList = registerRequestDTO.getChildList();
            for(RegisterRequestDTO c : childList){
                User child = insertUser(c, false);
                userRepository.save(child);

                ParentChild parentChild = ParentChild.builder()
                        .parent(parent)
                        .child(child)
                        .level1Reward(c.getLevel1Reward())
                        .level2Reward(c.getLevel2Reward())
                        .level3Reward(c.getLevel3Reward())
                        .build();
                parentChildRepository.save(parentChild);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청");
        }
        return true;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception{
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }
        return LoginResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .token(jwtProvider.createToken(user.getEmail())).build();
    }

    @Transactional(readOnly = true)
    public FindUserResponseDTO getUserInfo(String email) {
        log.info("/user Service 진입");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new KookChildException(ExceptionStatus.NOT_EXIST_USER_EMAIL));

        return FindUserResponseDTO.from(user);
    }


    private User insertUser(RegisterRequestDTO request, boolean isParent){
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNum(request.getPhoneNum())
                .ssn(request.getSsn())
                .birthdate(request.getBirthdate())
                .isParent(isParent)
                .build();
    }
}
