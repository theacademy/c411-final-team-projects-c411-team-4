package com.mthree.flighttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FlighttrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlighttrackerApplication.class, args);
	}

}
