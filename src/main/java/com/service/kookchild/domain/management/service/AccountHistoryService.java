package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.dto.FindAccountHistoryResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountHistoryService {
    private final AccountHistoryRepository accountHistoryRepository;

    public FindAccountHistoryResponse findAccountHistories(Authentication authentication){
        String email = getEmail(authentication);
        List<AccountHistory> accountHistoriesByUserId = accountHistoryRepository.findAccountHistoriesByUserId();

        return new FindAccountHistoryResponse();
    }

    private String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }
}
