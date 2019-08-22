package com.sci.services.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class WithdrawApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithdrawApplication.class, args);
	}

}
