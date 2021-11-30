package com.mongo.sofsit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SofsitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SofsitApplication.class, args);
	}

}
