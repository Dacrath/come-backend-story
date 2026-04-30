package com.example.come_backend_story;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComeBackendStoryApplication {

	static void main(String[] args) {
		// In your code
		SpringApplication.run(ComeBackendStoryApplication.class, args);
	}

}
