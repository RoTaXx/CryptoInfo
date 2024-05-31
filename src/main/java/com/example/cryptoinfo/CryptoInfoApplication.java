package com.example.cryptoinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoInfoApplication.class, args);
	}

}
