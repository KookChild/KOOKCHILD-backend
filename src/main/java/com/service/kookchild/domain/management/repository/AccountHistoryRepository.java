package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {

}
