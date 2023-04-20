package com.academicdashboard.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BackendApplication {

    //Controller -> Service -> Repository -> Database
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
    
    //Testing Method 
    @GetMapping("/greeting")
    public String greeting() {return "2nd Testing for Spring Boot";}
}
