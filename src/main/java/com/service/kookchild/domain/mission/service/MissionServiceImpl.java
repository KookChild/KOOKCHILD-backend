package com.service.kookchild.domain.mission.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.service.kookchild.domain.mission.dto.MissionCreateDTO;
import com.service.kookchild.domain.mission.repository.MissionChildRepository;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.repository.MissionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService{

	private final ParentChildRepository parentChildRepository;
	private final MissionChildRepository missionChildRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;

	@Override  @Transactional
	public void saveMission(MissionCreateDTO m, String email) {
		System.out.println("99999999999999999999999999999999999999");

		Optional<User> parent = userRepository.findByEmail(email);
		System.out.println("parent ID :: "+parent.get().getId());
		//List<Long> longList = new ArrayList<>();
		//m.getChildId = "22,23" 를 [22,23]으로 변환
		List<Long> childIdList = Arrays.stream(m.getChildIds().split(","))
				.map(Long::parseLong)
				.collect(Collectors.toList());
		System.out.println(childIdList);

		User Parent = parent.get();
		for(long childId : childIdList){ // id 리스트에 있는 모든 childId에 미션을 등록
			System.out.println(childId);
			Optional<User> child = userRepository.findById(childId);
			System.out.println("child ID :: "+child.get().getName());
			ParentChild pc = parentChildRepository.findByParentAndChild(Parent,child.get());
			System.out.println(pc.getId());

			Mission mission = Mission.builder().
					parentChild(pc).
					title(m.getTitle()).
					content(m.getContent()).
					reward(m.getReward()).
					image(m.getImage()).
					startDate(m.getStartDate()).
					endDate(m.getEndDate()).

					build();
			missionRepository.save(mission);
		}

//		Optional<User> child = userRepository.findById( (long) m.getChildId() );
//
//		ParentChild pc = parentChildRepository.findByParentAndChild(parent.get(),child.get());
//
//
//		Mission mission = Mission.builder().
//				parentChild(pc).
//				title(m.getTitle()).
//				content(m.getContent()).
//				reward(m.getReward()).
//				image(m.getImage()).
//				startDate(m.getStartDate()).
//				endDate(m.getEndDate()).
//
//				build();
//		missionRepository.save(mission);
	}
}
