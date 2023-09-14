package com.service.kookchild.domain.reward.repository;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.dto.FindAccountChildNameId;
import com.service.kookchild.domain.management.dto.FindAccountDTO;
import com.service.kookchild.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;

@Repository
@Transactional
public interface RewardRepository extends JpaRepository<Account, Long> {

}
