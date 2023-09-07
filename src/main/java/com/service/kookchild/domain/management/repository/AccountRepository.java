package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account, Long> {

}
