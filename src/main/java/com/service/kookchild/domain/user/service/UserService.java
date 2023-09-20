package com.service.kookchild.domain.user.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.security.JwtProvider;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.dto.*;
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
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentChildRepository parentChildRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO){
        User parent = insertParent(registerRequestDTO, true);
        userRepository.save(parent);
        createDepositAccount(parent, registerRequestDTO.getAccountPassword());
        List<RegisterChildDTO> childList = registerRequestDTO.getChildList();
        for(RegisterChildDTO c : childList){
            User child = insertChild(c, false);
            userRepository.save(child);

            createDepositAccount(child, c.getAccountPassword());
            createRewardAccount(child, c.getAccountPassword());

            ParentChild parentChild = ParentChild.builder()
                    .parent(parent)
                    .child(child)
                    .level1Reward(c.getLevel1Reward())
                    .level2Reward(c.getLevel2Reward())
                    .level3Reward(c.getLevel3Reward())
                    .build();
            parentChildRepository.save(parentChild);
        }
        return RegisterResponseDTO.builder()
                .isParent(true)
                .token(jwtProvider.createToken(parent.getEmail())).build();
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception{
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }
        return LoginResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .isParent(user.isParent())
                .token(jwtProvider.createToken(user.getEmail())).build();
    }

    @Transactional(readOnly = true)
    public FindUserResponseDTO getUserInfo(String email) {
        log.info("/user Service 진입");

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new KookChildException(ExceptionStatus.NOT_EXIST_USER_EMAIL));

        return FindUserResponseDTO.from(user);
    }

    @Transactional
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }


    private User insertParent(RegisterRequestDTO request, boolean isParent){
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

    private User insertChild(RegisterChildDTO request, boolean isParent){
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

    private void createDepositAccount(User user, String password){
        String accountNum = generateAccountNumber();
        Account account = Account.builder()
                .user(user)
                .accountName(user.getName())
                .balance(1000000)
                .password(password)
                .type(AccountType.입출금)
                .accountNum(accountNum).build();
        accountRepository.save(account);
    }

    private void createRewardAccount(User user, String password){
        String accountNum = generateAccountNumber();
        Account account = Account.builder()
                .user(user)
                .accountName(user.getName())
                .balance(1000000)
                .password(password)
                .type(AccountType.보상금)
                .accountNum(accountNum).build();
        accountRepository.save(account);
    }

    private static String generateAccountNumber() {
        UUID uuid = UUID.randomUUID();
        long leastBits = uuid.getLeastSignificantBits();
        long mostBits = uuid.getMostSignificantBits();

        String part1 = String.format("%06d", Math.abs(leastBits % 1_000_000L));
        String part2 = String.format("%02d", Math.abs(mostBits % 100L));
        String part3 = String.format("%06d", Math.abs((mostBits / 100L) % 1_000_000L));

        return part1 + "-" + part2 + "-" + part3;
    }
}
