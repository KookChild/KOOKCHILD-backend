package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
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

    @Override
    public FindAccountResponse sendMoney(FindAccountInfoPair fi) {
        FindAccountResponse fr = null;
        accountRepository.updateParentBalance(Long.parseLong(fi.getParentId()));
        accountRepository.updateChildBalance(Long.parseLong(fi.getChildId()));
        fr = accountRepository.sendMoney(Long.parseLong(fi.getChildId()));
        return fr;
    }
}
