package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Repository
@Transactional
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {

    List<AccountHistory> findAccountHistoriesByAccount(Account account);
   @Query("SELECT TO_CHAR(NVL(SUM(a.amount), 0), 'FM999,999,999,999') AS amount FROM AccountHistory a WHERE a.category IN(:name) AND a.userId = :id AND (a.createdDate <= :secondDate AND a.createdDate >= :firstDate) ")
    String findAmount(@Param("id") Long id, @Param("name") String name, @Param("firstDate")LocalDateTime firstDate, @Param("secondDate")LocalDateTime secondDate);

    @Query("SELECT TO_CHAR(NVL(SUM(a.amount), 0),'FM999,999,999,999') AS amount FROM AccountHistory a WHERE a.category NOT IN(:name) AND a.userId = :id AND (a.createdDate <= :secondDate AND a.createdDate >= :firstDate) ")
    String findNotInAmount(@Param("id") Long id, @Param("name") String name, @Param("firstDate")LocalDateTime firstDate, @Param("secondDate")LocalDateTime secondDate);
    List<AccountHistory> findAccountHistoriesByUserId(Long userId);

    @Query("SELECT TO_CHAR(NVL(SUM(ah.amount), 0), 'FM999,999,999,999') as rewardCompleteAmount\n" +
            "FROM AccountHistory ah \n" +
            "WHERE ah.category = '리워드' AND ah.userId = :userId")
    String rewardCompleteAmount(@Param("userId") Long userId);

    @Query("SELECT NVL(SUM(ah.amount), 0) as rewardCompleteAmount\n" +
            "FROM AccountHistory ah \n" +
            "WHERE ah.category = '리워드' AND ah.userId = :userId")
    Long rewardCompleteAmountLong(@Param("userId") Long userId);

    List<AccountHistory> findAccountHistoriesByCategoryAndUserId(String category, Long userId);

}
