package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WeatherapiiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherapiiApplication.class, args);
	}
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@PostConstruct
	public void generateHash() {
	    System.out.println(passwordEncoder.encode("admin123"));
	}
	
	
}
