package com.service.kookchild.domain.mission.service;


import com.service.kookchild.domain.mission.dto.MissionCreateDTO;
import org.springframework.stereotype.Service;
import com.service.kookchild.domain.mission.domain.Mission;


@Service
public interface MissionService {


	void saveMission(MissionCreateDTO m, String email);


}
