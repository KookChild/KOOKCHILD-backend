package com.service.kookchild.domain.mission.service;

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

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService{

	private final ParentChildRepository parentChildRepository;
	private final MissionChildRepository missionChildRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;

	@Override  @Transactional
	public void saveMission(MissionCreateDTO m, String email) {

		Optional<User> parent = userRepository.findByEmail(email);
		Optional<User> child = userRepository.findById( (long) m.getChildId() );

		ParentChild pc = parentChildRepository.findByParentAndChild(parent.get(),child.get());


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
}
