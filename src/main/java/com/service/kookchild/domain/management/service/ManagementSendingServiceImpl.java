package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;

import com.service.kookchild.domain.management.dto.*;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.UserRepository;
import com.service.kookchild.global.exception.ExceptionStatus;
import com.service.kookchild.global.exception.KookChildException;
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
    private final UserRepository userRepository;

    @Override
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi) {
        FindAccountDTO findAccountDTO = null;
        Long pId = Long.parseLong(fi.getParentId());
        Long cId = Long.parseLong(fi.getChildId());
        accountRepository.updateParentBalance(pId, fi.getAmount());
        accountRepository.updateChildBalance(cId, fi.getAmount());
        Account a  = accountRepository.findByUserId(cId);
        AccountHistory accountHistory = AccountHistory.builder()
                .userId(cId)
                .isDeposit(1)
                .category("예금")
                .amount(fi.getAmount())
                .targetName("").build();
        accountHistoryRepository.save(accountHistory);
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
        FindAccountDTO findAccountDTO = null;
        CheckChildMoneyResponse checkChildMoneyResponse = null;

        findAccountDTO = accountRepository.checkChildMoney(Long.parseLong(fi.getChildId()));
        if(findAccountDTO != null) {
            String name = userRepository.findNameById(Long.parseLong(fi.getChildId()));
            findAccountDTO.setUserName(name);


            System.out.println("accountNum : " + findAccountDTO.getAccountNum());
            Long amount = accountHistoryRepository.findAmount(Long.parseLong(fi.getChildId().trim()), "예금");
            Long notInAmount = accountHistoryRepository.findNotInAmount(Long.parseLong(fi.getChildId().trim()), "예금");
            System.out.println(amount + ", " + notInAmount);

            checkChildMoneyResponse =
                    CheckChildMoneyResponse.of(findAccountDTO.getAccountNum(), findAccountDTO.getUserName(), amount, notInAmount);
            System.out.println(findAccountDTO.getAccountNum());
        }


        return checkChildMoneyResponse;
    }

    @Override
    public ArrayList<FindAccountChildNameId> findChildNamesByParentId(Long id) {
        ArrayList<FindAccountChildNameId> list = null;
        list = accountRepository.findChildNamesByParentId(id);
        return list;
    }
}
