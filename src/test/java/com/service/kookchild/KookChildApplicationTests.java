package com.service.kookchild;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import com.service.kookchild.domain.challenge.service.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.domain.MissionTest;
import com.service.kookchild.domain.mission.service.MissionServiceImpl;

@SpringBootTest
@Transactional
@Commit
class KookChildApplicationTests {
	/**
	 * mission Test
	 */
	@Autowired //mission
	private MissionServiceImpl missionServiceImpl;




	}

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeService challengeService;



	@Test
	void 김지은테스트1(){
		List<Challenge> all = challengeRepository.findAll();
		System.out.println(all);
	}

	@Test
	void 김지은테스트2(){
		List<Challenge> list = challengeService.getChallengeListByChildId(1L);
		System.out.println(list);
	}
	@Test
	void 김지은테스트3(){
		Challenge challenge = challengeRepository.findChallengeById(1L);
		System.out.println(challenge);
	}




	/**
	 * mission test
	 */
	@Test
	void missionTest() {


		MissionTest m1 = new MissionTest("title1","content1",1000, new Date());
		MissionTest m2 = new MissionTest("title2","content2",2000, new Date());
		MissionTest m3 = new MissionTest("title3","content3",3000, new Date());
		MissionTest m4 = new MissionTest("title4","content4",4000, new Date());

		/*
		missionServiceImpl.saveMission(m1);
		missionServiceImpl.saveMission(m2);
		missionServiceImpl.saveMission(m3);
		missionServiceImpl.saveMission(m4);
		*/

		Mission mm = new Mission(23l, null,null,null,null,null,null,null,false,false);
		missionServiceImpl.saveMission(mm); // 자동으로 jpa 들어감
	}
	/**
	 * mission Test End
	 */

}


