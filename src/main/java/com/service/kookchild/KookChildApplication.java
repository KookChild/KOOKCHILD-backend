package com.service.kookchild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaAuditing
public class KookChildApplication {

	public static void main(String[] args) {
		SpringApplication.run(KookChildApplication.class, args);
	}

}
