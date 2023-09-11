package com.service.kookchild.domain.mission.service;

import com.service.kookchild.domain.mission.dto.MissionDetailDTO;
import com.service.kookchild.domain.mission.dto.MissionChildListDTO;

public interface MissionChildService {
    MissionChildListDTO getMissionList(String email, String state);
    MissionDetailDTO getMission(String email, long missionId);

    void requestMissionConfirm(String email, long missionId);

}
