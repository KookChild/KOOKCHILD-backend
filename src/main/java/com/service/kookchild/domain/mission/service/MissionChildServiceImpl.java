package com.service.kookchild.domain.mission.service;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.dto.*;
import com.service.kookchild.domain.mission.exception.MissionNotFoundException;
import com.service.kookchild.domain.mission.repository.MissionChildRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionChildServiceImpl implements MissionChildService{

    private final ParentChildRepository parentChildRepository;
    private final MissionChildRepository missionChildRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public MissionChildListDTO getMissionList(String email, String state) {
        List<MissionChildViewDTO> missionDTOList = new ArrayList<>();
        User user = findUser(email);

        ParentChild child = findParentChildByChildId(user.getId());
        List<Mission> missionList = new ArrayList<>();
        if(state.equals("newest")) {
            missionList = missionChildRepository.findByParentChildOrderByEndDateDesc(child);
        } else if(state.equals("oldest")){
            missionList = missionChildRepository.findByParentChildOrderByEndDate(child);
        }
        missionList.stream().forEach((e) -> {
            missionDTOList.add(MissionChildViewDTO.of(e));
        });

        MissionChildListDTO missionChildListDTO = MissionChildListDTO.builder()
                .missionAmount(missionDTOList.size())
                .missionLists(missionDTOList)
                .build();
        return missionChildListDTO;
    }

    @Override
    @Transactional
    public MissionDetailDTO getMission(String email, long missionId) {
        User user = findUser(email);

        Mission mission = missionChildRepository.findById(missionId).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );

        if (mission.getParentChild().getChild() != user || mission.getParentChild().getParent() != user) {
            return null;
        }

        boolean isParent = false;
        String childName = "";
        if(user.isParent()) {
            isParent = true;
            childName = mission.getParentChild().getChild().getName();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm");
        String formattedDate = mission.getModifiedDate().format(formatter);

        MissionDetailDTO missionDetailDTO = MissionDetailDTO.builder()
                .childName(childName)
                .title(mission.getTitle())
                .content(mission.getContent())
                .reward(mission.getReward()+"원")
                .image(mission.getImage())
                .startDate(mission.getStartDate())
                .endDate(mission.getEndDate())
                .completeDate(formattedDate)
                .parentConfirm(mission.isParentConfirm())
                .childConfirm(mission.isChildConfirm())
                .isParent(isParent)
                .build();
        return missionDetailDTO;
    }

    @Override
    @Transactional
    public boolean requestMissionConfirm(String email, long missionId) {
        User user = findUser(email);
        ParentChild child = findParentChildByChildId(user.getId());
        Mission mission = findMissionByChildId(missionId, child);
        if(mission.isChildConfirm()) return false;
        mission.requestConfirm(true);
        return true;
    }

    @Override
    @Transactional
    public boolean updateMission(String email, MissionUpdateDTO missionUpdateDTO) {
        Mission mission = missionChildRepository.findById(missionUpdateDTO.getMissionId()).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        User user = findUser(email);
        if(mission.getParentChild().getParent().getId()!=user.getId()) return false;
        mission.setMission(missionUpdateDTO);
        return true;
    }

    @Override
    public boolean deleteMission(String email, MissionDTO missionDTO) {
        User user = findUser(email);
        Mission mission = missionChildRepository.findById(missionDTO.getMissionId()).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        if(mission.getParentChild().getParent().getId()!=user.getId()) return false;
        missionChildRepository.delete(mission);
        return true;
    }


    private ParentChild findParentChildByChildId(long childId){
        ParentChild child = parentChildRepository.findByChildId(childId)
                .orElseThrow(() -> new EntityNotFoundException("주어진 childId에 해당하는 ParentChild가 존재하지 않습니다."));
        return child;
    }

    private ParentChild findParentChildByParentId(long parentId){
        ParentChild parent = parentChildRepository.findByParentId(parentId)
                .orElseThrow(() -> new EntityNotFoundException("주어진 parentId에 해당하는 ParentChild가 존재하지 않습니다."));
        return parent;
    }
    private Mission findMissionByChildId(long missionId, ParentChild child){
        return missionChildRepository.findByIdAndParentChild(missionId, child);
    }

    private User findUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않은 유저입니다.")
        );
        return user;
    }
}








