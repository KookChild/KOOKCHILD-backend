package com.service.kookchild.domain.user.repository;

import com.service.kookchild.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findById(String id);
    @Query("SELECT u.name FROM User u WHERE id = :id")
    String findNameById(@Param("id")Long id);

    boolean existsByEmail(String email);
}
