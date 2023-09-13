package com.service.kookchild.domain.mission.repository;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionChildRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByParentChild(ParentChild parentChild);
    List<Mission> findByParentChildOrderByEndDate(ParentChild parentChild);
    List<Mission> findByParentChildOrderByEndDateDesc(ParentChild parentChild);
    List<Mission> findByParentChildIdIn(List<Long> parentChildIds);

    // type이 있을 때
    List<Mission> findByParentChildAndParentConfirm(ParentChild parentChild, boolean parentConfirm);
    List<Mission> findByParentChildAndParentConfirmOrderByEndDate(ParentChild parentChild, boolean parentConfirm);
    List<Mission> findByParentChildAndParentConfirmOrderByEndDateDesc(ParentChild parentChild, boolean parentConfirm);

    Mission findByIdAndParentChild(long missionId, ParentChild parentChild);
    List<Mission> findByParentChildIdInAndParentConfirm(List<Long> parentChildIds, boolean parentConfirm);



}
