package com.service.kookchild.domain.user.repository;

import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentChildRepository extends JpaRepository<ParentChild, Long> {

    Optional<ParentChild> findByChildId(Long childId);
    List<ParentChild> findByParent(User parent);

    ParentChild findByParentAndChild(User parent, User child);
    ParentChild findByChild(User child);
}
