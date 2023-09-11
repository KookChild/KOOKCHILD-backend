package com.service.kookchild.domain.mission.repository;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionChildRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByParentChild(ParentChild parentChild);
    List<Mission> findByParentChildOrderByEndDate(ParentChild parentChild);
    List<Mission> findByParentChildOrderByEndDateDesc(ParentChild parentChild);

    Mission findByIdAndParentChild(long missionId, ParentChild parentChild);


}
