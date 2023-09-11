package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository
@Transactional
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
    @Query("SELECT SUM(a.amount) FROM AccountHistory a WHERE a.category IN(:name) AND a.userId = :id")
    Long findAmount(@Param("id") Long id, @Param("name") String name);
}
