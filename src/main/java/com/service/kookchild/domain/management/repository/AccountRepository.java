package com.service.kookchild.domain.management.repository;

import java.util.ArrayList;
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
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findUserId(@Param("email") String email);

    @Query("SELECT u.name FROM User u WHERE u.id = :id")
    String findUserNameById(@Param("id")Long id);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = 1 AND u.id = :parentId)")
    void updateParentBalance(@Param("parentId") Long parentId, @Param("amount") Long amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.user.id = (SELECT u.id FROM User u WHERE u.isParent = 0 AND u.id = :childId)")
    void updateChildBalance(@Param("childId") Long childId, @Param("amount") Long amount);

    @Query( "SELECT new com.service.kookchild.domain.management.dto.FindAccountResponse(a.balance AS balance, a.accountNum AS accountNum, a.user.name AS userName) FROM Account a WHERE a.user.id = :childId")
    FindAccountResponse checkChildMoney(@Param("childId") Long childId);

    @Query ("SELECT u.name FROM User u WHERE u.id IN (SELECT pc.child.id FROM ParentChild pc WHERE pc.parent.id = :id)")
    ArrayList<String> findChildNamesByParentId(@Param("id") Long id);

}
