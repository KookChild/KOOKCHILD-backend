package com.service.kookchild.domain.mission.service;

import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    @Override
    @Transactional
    public MissionChildListDTO getMissionList(String email, String state) {
        User user = findUser(email);
        ParentChild child = findParentChildByChildId(user.getId());

        List<Mission> requestMissionList = missionChildRepository.findByParentChildAndParentConfirmAndAndChildConfirm(child, false, true);
        List<Mission> ongoingMissionList = sortMissionsByState(child, state);
        List<MissionViewDTO> requestMissionDTOList = requestMissionList.stream()
                .map(MissionViewDTO::of)
                .collect(Collectors.toList());
        List<MissionViewDTO> ongoinMissionDTOList = ongoingMissionList.stream()
                .map(MissionViewDTO::of)
                .collect(Collectors.toList());

        return MissionChildListDTO.builder()
                .requestMissionLists(requestMissionDTOList)
                .ongoingMissionLists(ongoinMissionDTOList)
                .build();
    }

    @Override
    @Transactional
    public MissionParentListDTO getParentMissionList(String email, long child, String type) {
        User user = findUser(email);
        List<User> childList = parentChildRepository.findByParent(user)
                .stream()
                .map(ParentChild::getChild)
                .collect(Collectors.toList());


        List<Mission> missionList = (child == 0) ? getMissionsForAllChildren(user, type)
                : getMissionsForSpecificChild(child, type);

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
    public int confirmMission(String email, long missionId) {
        User user = findUser(email);
        Mission mission = missionChildRepository.findById(missionId).orElseThrow(
                () -> new MissionNotFoundException("해당 미션이 존재하지 않습니다.")
        );
        if(!mission.getParentChild().getParent().equals(user)) return 403;
        if(mission.isParentConfirm()) return 400;
        Account parentAccount = accountRepository.findByUser(user);
        long missionReward = Integer.parseInt(mission.getReward());
        User child = mission.getParentChild().getChild();
        if(missionReward <= parentAccount.getBalance()){
            mission.approveConfirm(true);
            accountRepository.updateParentBalance(user.getId(), missionReward);
            accountRepository.updateChildBalance(child.getId(), missionReward);
            AccountHistory childHistory = AccountHistory.builder()
                    .userId(child.getId())
                    .isDeposit(1)
                    .amount(missionReward)
                    .targetName("부모")
                    .category("리워드")
                    .account(accountRepository.findByUser(child)).build();
            accountHistoryRepository.save(childHistory);
        } else{
            return 422;
        }
        return 200;
    }

    @Override
    @Transactional
    public MissionHistoryDTO getMissionHistoryList(String email, String state) {
        User user = findUser(email);
        ParentChild child = findParentChildByChildId(user.getId());

        List<Mission> successMissionList = sortHistoryMissionsByState(child, state);
        List<MissionViewDTO> successMissionDTOList = successMissionList.stream()
                .map(MissionViewDTO::of)
                .collect(Collectors.toList());

        return MissionHistoryDTO.builder()
                .successMissionList(successMissionDTOList).build();
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
        if(state.equals("newest")){
            return missionChildRepository.findByParentChildAndParentConfirmAndChildConfirmOrderByEndDateDesc(child, false, false);
        } else{
            return missionChildRepository.findByParentChildAndParentConfirmAndChildConfirmOrderByEndDate(child, false, false);
        }
    }

    private List<Mission> sortHistoryMissionsByState(ParentChild child, String state) {
        if(state.equals("newest")){
            return missionChildRepository.findByParentChildAndParentConfirmAndChildConfirmOrderByEndDateDesc(child, true, true);
        } else{
            return missionChildRepository.findByParentChildAndParentConfirmAndChildConfirmOrderByEndDate(child, true, true);
        }
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


    private List<Mission> getMissionsForAllChildren(User parent, String type) {
        List<ParentChild> relations = findParentChildByParentId(parent);
        List<Long> parentChildIds = relations.stream()
                .map(ParentChild::getId)
                .collect(Collectors.toList());
        if(type.equals("requested")) return missionChildRepository.findByParentChildIdInAndParentConfirmAndAndChildConfirm(parentChildIds, false, true);
        else if(type.equals("ongoing")) return missionChildRepository.findByParentChildIdInAndParentConfirm(parentChildIds, false);
        else return missionChildRepository.findByParentChildIdInAndParentConfirm(parentChildIds, true);
    }

    private List<Mission> getMissionsForSpecificChild(long childId, String type) {
        User c = userRepository.findById(childId).orElseThrow(
                () -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.")
        );
        ParentChild selectedChild = findParentChildByChildId(c.getId());
        if(type.equals("requested")) return missionChildRepository.findByParentChildAndParentConfirmAndAndChildConfirm(selectedChild, false, true);
        if (type.equals("ongoing")) return missionChildRepository.findByParentChildAndParentConfirm(selectedChild, false);
        else return missionChildRepository.findByParentChildAndParentConfirm(selectedChild, true);
    }

}

