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
import javax.persistence.criteria.CriteriaBuilder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionChildServiceImpl implements MissionChildService{

    private final ParentChildRepository parentChildRepository;
    private final MissionChildRepository missionChildRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public MissionChildListDTO getMissionList(String email, String state) {
        User user = findUser(email);
        ParentChild child = findParentChildByChildId(user.getId());

        List<Mission> missionList = sortMissionsByState(child, state);
        List<MissionViewDTO> missionDTOList = missionList.stream()
                .map(MissionViewDTO::of)
                .collect(Collectors.toList());

        return MissionChildListDTO.builder()
                .missionAmount(missionDTOList.size())
                .missionLists(missionDTOList)
                .build();
    }

    @Override
    @Transactional
    public MissionParentListDTO getParentMissionList(String email, long child) {
        User user = findUser(email);
        List<User> childList = parentChildRepository.findByParent(user)
                .stream()
                .map(ParentChild::getChild)
                .collect(Collectors.toList());


        List<Mission> missionList = (child == 0) ? getMissionsForAllChildren(user)
                : getMissionsForSpecificChild(child);

        List<MissionViewDTO> missionDTOList = toMissionViewDTOList(missionList);
        List<ChildListDTO> childListDTOList = childList.stream()
                .map(ChildListDTO::of)
                .collect(Collectors.toList());

        return MissionParentListDTO.builder()
                .missionAmount(missionDTOList.size())
                .childLists(childListDTOList)
                .missionLists(missionDTOList)
                .build();
    }

    @Override
    @Transactional
    public MissionDetailDTO getMission(String email, long missionId) {
        User user = findUser(email);

        Mission mission = missionChildRepository.findById(missionId).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );

        if (!mission.getParentChild().getChild().equals(user) && !mission.getParentChild().getParent().equals(user)) {
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
                .reward(mission.getReward())
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
        Mission mission = findMissionByParentChild(missionId, child);
        if(mission.isChildConfirm()) return false;
        mission.requestConfirm(true);
        return true;
    }

    @Override
    @Transactional
    public boolean confirmMission(String email, long missionId) {
        User user = findUser(email);
        Mission mission = missionChildRepository.findById(missionId).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        if(!mission.getParentChild().getParent().equals(user)) return false;
        if(mission.isParentConfirm()) return false;
        mission.approveConfirm(true);
        return true;
    }

    @Override
    @Transactional
    public boolean updateMission(String email, MissionUpdateDTO missionUpdateDTO) {
        Mission mission = missionChildRepository.findById(missionUpdateDTO.getMissionId()).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        User user = findUser(email);
        if(!mission.getParentChild().getParent().equals(user)) return false;
        mission.setMission(missionUpdateDTO);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteMission(String email, MissionDTO missionDTO) {
        User user = findUser(email);
        Mission mission = missionChildRepository.findById(missionDTO.getMissionId()).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        if(!mission.getParentChild().getParent().equals(user)) return false;
        missionChildRepository.delete(mission);
        return true;
    }

    private List<Mission> sortMissionsByState(ParentChild child, String state) {
        return state.equals("newest") ? missionChildRepository.findByParentChildOrderByEndDateDesc(child)
                : missionChildRepository.findByParentChildOrderByEndDate(child);
    }

    private List<MissionViewDTO> toMissionViewDTOList(List<Mission> missionList) {
        return missionList.stream()
                .map(MissionViewDTO::of)
                .collect(Collectors.toList());
    }

    private User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않은 유저입니다.")
        );
    }

    private ParentChild findParentChildByChildId(long childId){
        return parentChildRepository.findByChildId(childId)
                .orElseThrow(() -> new EntityNotFoundException("주어진 childId에 해당하는 ParentChild가 존재하지 않습니다."));
    }


    private List<ParentChild> findParentChildByParentId(User parent){
        List<ParentChild> p = parentChildRepository.findByParent(parent);
        return p;
    }
    private Mission findMissionByParentChild(long missionId, ParentChild user){
        return missionChildRepository.findByIdAndParentChild(missionId, user);
    }


    private List<Mission> getMissionsForAllChildren(User parent) {
        List<ParentChild> relations = findParentChildByParentId(parent);
        List<Long> parentChildIds = relations.stream()
                .map(ParentChild::getId)
                .collect(Collectors.toList());
        return missionChildRepository.findByParentChildIdIn(parentChildIds);
    }

    private List<Mission> getMissionsForSpecificChild(long childId) {
        User c = userRepository.findById(childId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.")
        );
        ParentChild selectedChild = findParentChildByChildId(c.getId());
        return missionChildRepository.findByParentChild(selectedChild);
    }

}

