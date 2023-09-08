package com.service.spring;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.service.spring.domain.Mission;
import com.service.spring.service.MissionServiceImpl;

@SpringBootTest
@Transactional
@Commit
class KookchildLocalApplicationTests {

	@Autowired
	private MissionServiceImpl missionServiceImpl;
	
	@Test
	void contextLoads() {
		
		Mission m1 = new Mission("title1","content1",1000, new Date());
//		MissionTest m2 = new MissionTest("title2","content2",2000, new Date());
//		MissionTest m3 = new MissionTest("title3","content3",3000, new Date());
//		MissionTest m4 = new MissionTest("title4","content4",4000, new Date());
//		
//		
		System.out.println(m1);
//		System.out.println(m2);
//		System.out.println(m3);
//		System.out.println(m4);
		
		//MissionServiceImpl mm = new MissionServiceImpl();
		// 다 보이기
		missionServiceImpl.saveMission(m1);
//		mm.saveMission(m2);
//		mm.saveMission(m3);
//		mm.saveMission(m4);
		
		// 등록하기
	}

}
