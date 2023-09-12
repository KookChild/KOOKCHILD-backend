package com.service.kookchild.domain.mission.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.service.kookchild.domain.mission.domain.Mission;


public interface MissionRepository extends JpaRepository<Mission, String>{
	
//	@Query(value = "SELECT id, password, name, address FROM member WHERE id=?", nativeQuery = true)
//	Mission findByIdMission(String id);
	
}