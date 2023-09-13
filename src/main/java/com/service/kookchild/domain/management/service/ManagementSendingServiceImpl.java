package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;

import com.service.kookchild.domain.management.dto.CheckChildMoneyResponse;
import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountDTO;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementSendingServiceImpl implements ManagementSendingService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi) {
        FindAccountDTO findAccountDTO = null;
        Long pId = Long.parseLong(fi.getParentId());
        Long cId = Long.parseLong(fi.getChildId());
        accountRepository.updateParentBalance(pId, fi.getAmount());
        accountRepository.updateChildBalance(cId, fi.getAmount());
        Account a  = accountRepository.findByUserId(cId);
        accountHistoryRepository.save(new AccountHistory(cId, 1, fi.getAmount(), "", "예금"));
        findAccountDTO = accountRepository.checkChildMoney(Long.parseLong(fi.getChildId()));

        return findAccountDTO;
    }

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
    public CheckChildMoneyResponse checkChildMoney(FindAccountInfoPair fi) {
        System.out.println("sendChildMoney");
        FindAccountDTO findAccountDTO = accountRepository.checkChildMoney(Long.parseLong(fi.getChildId().trim()));
        Long amount = accountHistoryRepository.findAmount(Long.parseLong(fi.getChildId().trim()), "예금");
        Long notInAmount = accountHistoryRepository.findNotInAmount(Long.parseLong(fi.getChildId().trim()), "예금");
        System.out.println(amount+", "+ notInAmount);

        CheckChildMoneyResponse checkChildMoneyResponse =
                CheckChildMoneyResponse.of(findAccountDTO.getAccountNum(),findAccountDTO.getUserName(), amount,notInAmount);
        System.out.println(findAccountDTO.getAccountNum());
        return checkChildMoneyResponse;
    }

    @Override
    public ArrayList<String> findChildNamesByParentId(Long id) {
        ArrayList<String> list = null;
        list = accountRepository.findChildNamesByParentId(id);
        return list;
    }
}
