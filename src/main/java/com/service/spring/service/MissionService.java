package com.service.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.service.spring.domain.Mission;


@Service
public interface MissionService {
	
	
	void saveMission(Mission m); // 미션 만들어서 테이블 저장
	
	//jpa repo에서 자동 제공
	//List<Mission> showAllMission(); // 모든 미션 보이게 하는
	


}
