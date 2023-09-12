package com.service.kookchild;

import com.service.kookchild.domain.challenge.domain.Challenge;
import com.service.kookchild.domain.challenge.repository.ChallengeRepository;
import com.service.kookchild.domain.challenge.service.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
class KookChildApplicationTests {

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeService challengeService;
	@Test
	void contextLoads() {
	}

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
}
