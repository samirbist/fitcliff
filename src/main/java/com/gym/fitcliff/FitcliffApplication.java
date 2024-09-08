package com.gym.fitcliff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FitcliffApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitcliffApplication.class, args);
	}

}
