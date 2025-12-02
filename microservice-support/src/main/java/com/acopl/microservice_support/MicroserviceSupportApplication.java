package com.acopl.microservice_support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceSupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSupportApplication.class, args);
	}

}
