package com.service.kookchild.domain.mission.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.kookchild.domain.mission.domain.Mission;


public interface MissionRepository extends JpaRepository<Mission, String>{
	
//	@Query(value = "SELECT id, password, name, address FROM member WHERE id=?", nativeQuery = true)
//	Mission findByIdMission(String id);

    @Query("SELECT TO_CHAR(NVL(SUM(m.reward), 0), 'FM999,999,999,999')\n" +
            "FROM Mission m \n" +
            "WHERE m.parentChild.id = (\n" +
            "SELECT pc.id\n" +
            "FROM ParentChild pc \n" +
            "WHERE pc.child.id = :id) AND m.parentConfirm = false")
    String rewardConfirmWaitAmount(@Param("id") Long id);
	
}