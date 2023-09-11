package com.service.kookchild.domain.mission.service;

import com.service.kookchild.domain.mission.dto.MissionDTO;
import com.service.kookchild.domain.mission.dto.MissionDetailDTO;
import com.service.kookchild.domain.mission.dto.MissionChildListDTO;
import com.service.kookchild.domain.mission.dto.MissionUpdateDTO;

public interface MissionChildService {
    MissionChildListDTO getMissionList(String email, String state);
    MissionDetailDTO getMission(String email, long missionId);

    boolean requestMissionConfirm(String email, long missionId);

    boolean updateMission(String email, MissionUpdateDTO missionUpdateDTO);

    boolean deleteMission(String email, MissionDTO missionDTO);
}
