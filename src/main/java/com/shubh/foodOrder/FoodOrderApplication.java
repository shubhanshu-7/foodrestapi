package com.shubh.foodOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodOrderApplication.class, args);
	}

	public void printEnv() {
		System.out.println("MONGODB_URL: " + System.getenv("MONGODB_URL"));
		System.out.println("JWT_SECRET_KEY: " + System.getenv("JWT_SECRET_KEY"));
	}

}
