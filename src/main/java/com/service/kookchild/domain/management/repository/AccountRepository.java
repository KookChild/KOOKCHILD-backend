package com.service.kookchild.domain.management.repository;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - 1000 WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = 1 AND u.id = :parentId)")
    void updateParentBalance(@Param("parentId") String parentId);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + 1000 WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = 0 AND u.id = :childId)")
    void updateChildBalance(@Param("childId") String childId);


    @Query("SELECT a.balance, a.user.name, a.accountNum FROM Account a JOIN a.user u WHERE u.id = :childId")
    FindAccountResponse sendMoney(@Param("childId") String childId);

}
