package com.service.kookchild.domain.mission.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.repository.MissionRepository;

@Service
public class MissionServiceImpl implements MissionService{
	
	
	@Autowired
	public MissionRepository missionRepository;

//	@Override
//	public List<Mission> showAllMission() {
//		return missionRepository.findAll();
//	}
//	

	@Override
	public void saveMission(Mission m) {
		System.out.println("saveMission :: " + m);
		missionRepository.save(m);
		
	}
	
	
}
