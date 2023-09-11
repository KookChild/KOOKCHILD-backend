package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementSendingServiceImpl implements ManagementSendingService{

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    public FindAccountResponse sendChildMoney(FindAccountInfoPair fi) {
        FindAccountResponse fr = null;
        Long id = Long.parseLong(fi.getChildId());
        accountRepository.updateParentBalance(id);
        accountRepository.updateChildBalance(id);

        accountHistoryRepository.save(new AccountHistory(id, 1, 1000L, "", "예금"));
        fr = accountRepository.checkChildMoney(Long.parseLong(fi.getChildId()));

        return fr;
    }

    @Override
    public FindAccountResponse checkChildMoney(FindAccountInfoPair fi) {
        System.out.println("sendChildMoney");
        return accountRepository.checkChildMoney(Long.parseLong(fi.getChildId().trim()));
}
}
