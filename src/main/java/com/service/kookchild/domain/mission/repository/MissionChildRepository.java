package com.service.kookchild.domain.mission.repository;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.user.domain.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionChildRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByParentChildAndParentConfirmAndAndChildConfirm(ParentChild parentChild, boolean parentConfirm, boolean childConfirm);
    List<Mission> findByParentChildIdInAndParentConfirmAndAndChildConfirm(List<Long> parentChildIds, boolean parentConfirm, boolean childConfirm);

    // type이 있을 때
    List<Mission> findByParentChildAndParentConfirm(ParentChild parentChild, boolean parentConfirm);
    List<Mission> findByParentChildAndParentConfirmAndRewardReceiveOrderByEndDate(ParentChild parentChild, boolean parentConfirm, boolean rewardReceive);
    List<Mission> findByParentChildAndParentConfirmAndRewardReceiveOrderByEndDateDesc(ParentChild parentChild, boolean parentConfirm, boolean rewardReceive);

    Mission findByIdAndParentChild(long missionId, ParentChild parentChild);
    List<Mission> findByParentChildIdInAndParentConfirm(List<Long> parentChildIds, boolean parentConfirm);

    List<Mission> findByParentChildAndParentConfirmAndRewardReceive(ParentChild parentChild, boolean parentConfirm, boolean rewardReceive);


    List<Mission> findByParentChildAndChildConfirmOrderByEndDateDesc(ParentChild child, boolean childConfirm);

    List<Mission> findByParentChildAndChildConfirmOrderByEndDate(ParentChild child, boolean childConfirm);
}
