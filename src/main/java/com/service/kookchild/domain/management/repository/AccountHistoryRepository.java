package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {

    List<AccountHistory> findAccountHistoriesByAccount(Account account);
}
