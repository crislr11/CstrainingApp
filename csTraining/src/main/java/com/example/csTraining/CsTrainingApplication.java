package com.example.csTraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsTrainingApplication.class, args);
	}

}
