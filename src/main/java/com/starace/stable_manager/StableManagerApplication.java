package com.starace.stable_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StableManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StableManagerApplication.class, args);
	}

}
