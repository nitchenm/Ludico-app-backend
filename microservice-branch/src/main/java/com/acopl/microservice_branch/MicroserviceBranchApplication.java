package com.acopl.microservice_branch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceBranchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceBranchApplication.class, args);
	}

}
