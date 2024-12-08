package com.tinomaster.virtualdream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VirtualdreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualdreamApplication.class, args);

	}

}
