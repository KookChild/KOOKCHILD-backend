package com.service.kookchild.domain.management.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.dto.FindAccountChildNameId;
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
    @Query("SELECT a FROM Account a WHERE a.type = 1 AND a.user.id = :id")
    Optional<Account> findAccountsByType1AndUserId(@Param("id") Long id);

    @Query("SELECT a FROM Account a WHERE a.type = 2 AND a.user.id = :id")
    Optional<Account> findAccountByType2AndUserId(@Param("id") Long id);
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findUserId(@Param("email") String email);

    @Query("SELECT u.name FROM User u WHERE u.id = :id")
    String findUserNameById(@Param("id")Long id);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = true AND u.id = :parentId)")
    void updateParentBalance(@Param("parentId") Long parentId, @Param("amount") Long amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = false AND u.id = :childId) AND a.type=1")
    void updateChildBalance(@Param("childId") Long childId, @Param("amount") Long amount);

    @Query("SELECT NEW com.service.kookchild.domain.management.dto.FindAccountDTO(TO_CHAR(a.balance, 'FM999,999,999,999') AS balance, a.accountNum AS accountNum, u.name AS userName) " +
            "FROM Account a " +
            "JOIN User u ON u.id = a.user.id WHERE a.user.id = :childId AND a.type = 1")
    FindAccountDTO checkChildMoney(@Param("childId") Long childId);

    @Query ("SELECT DISTINCT NEW com.service.kookchild.domain.management.dto.FindAccountChildNameId(u.name, a.accountNum, u.id) FROM User u JOIN Account a ON u.id = a.user.id WHERE u.id IN (SELECT pc.child.id FROM ParentChild pc WHERE pc.parent.id = :id) AND a.type = 1")
    ArrayList<FindAccountChildNameId> findChildNamesByParentId(@Param("id") Long id);

    @Query("SELECT a FROM Account a WHERE a.user.id = :id")
    Account findByUserId(@Param("id") Long id);

    Account findByUser(User user);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.type = 2 AND a.user.id = (SELECT u.id FROM User u WHERE u.isParent = true AND u.id = :parentId)")
    void updateParentType1Balance(@Param("parentId") Long parentId, @Param("amount") Long amount);


    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.type = 2 AND a.user.id = (SELECT u.id FROM User u WHERE u.isParent = false AND u.id = :childId)")
    void updateChildType2Balance(@Param("childId") Long childId, @Param("amount") Long amount);


}
