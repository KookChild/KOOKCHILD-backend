package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.dto.FindAccountDTO;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.global.exception.ExceptionStatus;
import com.service.kookchild.global.exception.KookChildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementService {

    private final AccountHistoryRepository accountHistoryRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public FindAccountDTO getAccountInfo(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                ()-> new KookChildException(ExceptionStatus.NOT_EXIST_USER_EMAIL));

        Account account = accountRepository.findAccountByUser(user);

//        return FindAccountResponse.from(account);
        return new FindAccountDTO();
    }
}
