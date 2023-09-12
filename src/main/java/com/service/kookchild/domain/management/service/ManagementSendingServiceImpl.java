package com.service.kookchild.domain.management.service;

import antlr.collections.List;
import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementSendingServiceImpl implements ManagementSendingService{

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    public Long findUserId(String email) {
        Long id = accountRepository.findUserId(email);
        return id;
    }

    @Override
    public String findUserNameById(Long id) {
        String name = accountRepository.findUserNameById(id);
        return name;
    }


    @Override
    public FindAccountResponse sendChildMoney(FindAccountInfoPair fi) {
        FindAccountResponse fr = null;
        Long pId = Long.parseLong(fi.getParentId());
        Long cId = Long.parseLong(fi.getChildId());
        accountRepository.updateParentBalance(pId, fi.getAmount());
        accountRepository.updateChildBalance(cId, fi.getAmount());

        accountHistoryRepository.save(new AccountHistory(cId, 1, fi.getAmount(), "", "예금"));
        fr = accountRepository.checkChildMoney(Long.parseLong(fi.getChildId()));

        return fr;
    }

    @Override
    public FindAccountResponse checkChildMoney(FindAccountInfoPair fi) {
        System.out.println("sendChildMoney");
        return accountRepository.checkChildMoney(Long.parseLong(fi.getChildId().trim()));
    }

    @Override
    public Long FindConsumption(FindAccountInfoPair fi){
        Long consumption = null;
        consumption = accountHistoryRepository.findAmount(Long.parseLong(fi.getChildId().trim()), "예금");
        return consumption;
    }

    @Override
    public ArrayList<String> findChildNamesByParentId(Long id) {
        ArrayList<String> list = null;
        list = accountRepository.findChildNamesByParentId(id);
        return list;
    }
}
