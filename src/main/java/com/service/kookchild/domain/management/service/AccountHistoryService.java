package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.dto.FindAccountHistoryResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountHistoryService {
    private final AccountHistoryRepository accountHistoryRepository;
    private final AccountRepository accountRepository;

    public FindAccountHistoryResponse findAccountHistories(Authentication authentication){
        String email = getEmail(authentication);
        List<AccountHistory> accountHistoriesByUserId = accountHistoryRepository.findAccountHistoriesByUserId(1L);

        return new FindAccountHistoryResponse();
    }

    public void updateAccountHistoryIsWithdraw(Long id){
        List<AccountHistory> list =
                accountHistoryRepository.findAccountHistoriesByCategoryAndUserId("리워드", id);

        Long amount = 0L;

        if(list.size() != 0) {
            for (AccountHistory accountHistory : list) {
                amount += accountHistory.getAmount();
            }
            System.out.println(amount);

            List<AccountHistory> list2 =
                    list
                            .stream()
                            .map(history -> {
                                history.setCategory("리워드출금");
                                return history;
                            })
                            .collect(Collectors.toList());

            accountHistoryRepository.saveAll(list2);

            Account account = accountRepository.findAccountByType2AndUserId(id).get();
            account.setBalance(0L);

            Account account1 = accountRepository.findAccountsByType1AndUserId(id).get();
            account1.setBalance(account1.getBalance() + amount);

            accountRepository.save(account);
            accountRepository.save(account1);
        }

    }

    private String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();

        return principal.getEmail();
    }


}
