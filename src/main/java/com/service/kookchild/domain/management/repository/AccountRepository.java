package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.dto.FindAccountDTO;
import com.service.kookchild.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - 1000 WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = true AND u.id = :parentId)")
    void updateParentBalance(@Param("parentId") Long parentId);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + 1000 WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = false AND u.id = :childId)")
    void updateChildBalance(@Param("childId") Long childId);

    @Query("SELECT a.balance, a.accountNum, a.user.name FROM Account a JOIN a.user u WHERE u.id = :childId")
    FindAccountDTO checkChildMoney(@Param("childId") Long childId);

    Account findAccountByUser(User user);

}
