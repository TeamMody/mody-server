package com.example.mody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModyApplication.class, args);
	}

}
