package com.academicdashboard.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    //Controller -> Service -> Repository -> Database
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
