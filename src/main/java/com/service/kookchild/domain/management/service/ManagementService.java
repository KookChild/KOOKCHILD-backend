package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementService {

    private final AccountHistoryRepository accountHistoryRepository;
    private final AccountRepository accountRepository;

    public FindAccountResponse getAccountInfo(Long accountId){
        Account account = accountRepository.findById(accountId).get();
        List<AccountHistory> accountHistoriesByAccount = accountHistoryRepository.findAccountHistoriesByAccount(account);

        return FindAccountResponse.of(account,accountHistoriesByAccount);
    }
}
