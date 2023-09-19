package com.service.kookchild.domain.management.service;

import java.util.List;
import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;

import com.service.kookchild.domain.management.domain.AccountType;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagementSendingServiceImpl implements ManagementSendingService {

    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    @Transactional
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi) {
        FindAccountDTO findAccountDTO = null;
        Long pId = fi.getParentId();
        Long cId = fi.getChildId();
        System.out.println("check---1");
        accountRepository.updateParentBalance(pId, fi.getAmount());
        System.out.println("check---2");
        accountRepository.updateChildBalance(cId, fi.getAmount());
        System.out.println("check---3");

        Account account = accountRepository.findByUserIdAndType(cId, AccountType.입출금);
        AccountHistory accountHistory = AccountHistory.builder()
                .userId(cId)
                .isDeposit(1)
                .amount(fi.getAmount())
                .targetName("")
                .category("예금")
                .build();
        accountHistoryRepository.save(accountHistory);
        System.out.println("check---5");
        findAccountDTO = accountRepository.checkChildMoney(fi.getChildId());
        System.out.println("check---6");

        return findAccountDTO;
    }

    @Override
    public Long findUserId(String email) {
        Long id = accountRepository.findUserId(email);
        return id;
    }

    @Override
    @Transactional
    public ArrayList<LocalDateTime> getLastDayOf() {
        // 현재 날짜를 가져옵니다.
        LocalDateTime currentDate = LocalDateTime.now();

        // 현재 날짜의 년과 월을 가져옵니다.
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        // 다음 달을 계산합니다.
        YearMonth nextMonthYearMonth = YearMonth.of(currentYear, currentMonth).plusMonths(1);

        // 다음 달의 년과 월을 가져옵니다.
        int nextYear = nextMonthYearMonth.getYear();
        Month nextMonth = nextMonthYearMonth.getMonth();

        // 현재 달과 다음 달의 년월을 LocalDateTime으로 변환합니다.
        LocalDateTime currentMonthDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0);
        LocalDateTime nextMonthDateTime = LocalDateTime.of(nextYear, nextMonth, 1, 0, 0);

        // 결과를 출력합니다.
        System.out.println("현재 달의 LocalDateTime: " + currentMonthDateTime);
        System.out.println("다음 달의 LocalDateTime: " + nextMonthDateTime);

        ArrayList<LocalDateTime> list = new ArrayList<>();
        list.add(currentMonthDateTime);
        list.add(nextMonthDateTime);
        return list;
    }

    @Override
    @Transactional
    public FindAccountInformation findChildNamesByParentId(FindAccountInfoPair fi) {

        FindAccountInformation findAccountInformation = null;

        ArrayList<LocalDateTime> dates = getLastDayOf();

        ArrayList<FindAccountChildNameId> list = accountRepository.findChildNamesByParentId(fi.getParentId());

        List<Long> idList = list.stream()
                .map(FindAccountChildNameId :: getId)
                .collect(Collectors.toList());

        System.out.println("check---1");
        List<String> savingAmountList = idList.stream()
                .map(id ->
                        accountHistoryRepository.findAmount(id, "예금", dates.get(0), dates.get(1))
                )
                .collect(Collectors.toList());
        System.out.println("check---2");
        List<String> spendingAmountList = idList.stream()
                .map(id -> accountHistoryRepository.findNotInAmount(id, "예금", dates.get(0), dates.get(1)))
                .collect(Collectors.toList());
        System.out.println("check---3");

        for(int i=0;i<list.size();i++){
            String savingAmount = savingAmountList.get(i);
            String spendingAmount = spendingAmountList.get(i);

            FindAccountChildNameId fc = list.get(i);
            fc.setSavingAmount(savingAmount);
            fc.setSpendingAmount(spendingAmount);
        }
        System.out.println("check---4");
        Account account = accountRepository.findByUserId(fi.getParentId());
        System.out.println("check---5");
        findAccountInformation = new FindAccountInformation(account.getBalance(), list);

        return findAccountInformation;
    }
}
