package com.service.kookchild.domain.mission.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.service.kookchild.domain.chatgpt.service.ChatGptService;
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
	private final ChatGptService chatGptService;

	@Override  @Transactional
	public void saveMission(MissionCreateDTO m, String email) {
		Optional<User> parent = userRepository.findByEmail(email);
		System.out.println("parent ID :: "+parent.get().getId());
		List<Long> childIdList = Arrays.stream(m.getChildIds().split(","))
				.map(Long::parseLong)
				.collect(Collectors.toList());
		String question = "What is the English term for '"+m.getTitle()+"'? Answer only with the English words.";
		String missionTitle = chatGptService.sendRequestToChatGPT(question);
		String questionToGpt = "I'm going to give the child a mission called "+missionTitle+". Please provide an image that, when a child sees it, would remind them of the phrase "+missionTitle+".";
		String imageUrl = chatGptService.makeImages(questionToGpt);

		User Parent = parent.get();
		for(long childId : childIdList){
			Optional<User> child = userRepository.findById(childId);
			ParentChild pc = parentChildRepository.findByParentAndChild(Parent,child.get());

			Mission mission = Mission.builder().
					parentChild(pc).
					title(m.getTitle()).
					content(m.getContent()).
					reward(m.getReward()).
					image(m.getImage()).
					startDate(m.getStartDate()).
					endDate(m.getEndDate()).
					image(imageUrl).
					build();
			missionRepository.save(mission);
		}
	}
}
