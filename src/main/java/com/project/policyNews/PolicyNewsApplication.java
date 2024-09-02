package com.project.policyNews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PolicyNewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolicyNewsApplication.class, args);
	}

}
