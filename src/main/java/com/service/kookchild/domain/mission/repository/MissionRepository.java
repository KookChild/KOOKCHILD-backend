package com.service.kookchild.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.kookchild.domain.mission.domain.Mission;

import java.util.List;


public interface MissionRepository extends JpaRepository<Mission, String>{
	
//	@Query(value = "SELECT id, password, name, address FROM member WHERE id=?", nativeQuery = true)
//	Mission findByIdMission(String id);

    @Query("SELECT TO_CHAR(NVL(SUM(m.reward), 0), 'FM999,999,999,999')\n"+
            "FROM com.service.kookchild.domain.mission.domain.Mission m \n"+
            "WHERE m.parentChild.id = (\n"+
            "SELECT pc.id\n"+
            "FROM com.service.kookchild.domain.user.domain.ParentChild pc\n"+
            "WHERE pc.child.id = :id\n"+
            ") AND (m.parentConfirm = 0 AND m.childConfirm = 1)")
    String rewardConfirmWaitAmount(@Param("id") Long id);

    List<Mission> findByParentChildIdAndChildConfirm(Long parentChildId, boolean childConfirm);

    @Query("SELECT TO_CHAR(NVL(SUM(m.reward), 0), 'FM999,999,999,999')\n"+
            "FROM com.service.kookchild.domain.mission.domain.Mission m \n"+
            "WHERE m.parentChild.id = (\n"+
            "SELECT pc.id\n"+
            "FROM com.service.kookchild.domain.user.domain.ParentChild pc\n"+
            "WHERE pc.child.id = :id\n"+
            ") AND (m.parentConfirm = 0 AND m.childConfirm = 0)")
    String notCompletedMissionAmount(@Param("id") Long id);

}