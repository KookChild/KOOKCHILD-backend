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
    public void updateParentBalance(String parentId) {
        accountRepository.updateParentBalance(parentId);
    }

    @Override
    public void updateChildBalance(String childId) {
        accountRepository.updateChildBalance(childId);
    }

    @Override
    public FindAccountResponse sendMoney(FindAccountInfoPair fi) {
        FindAccountResponse fr = null;
        try{
            fr = accountRepository.sendMoney(String.valueOf(fi.getChildId()));
        }catch(Exception e){

        }
        return fr;
    }
}
