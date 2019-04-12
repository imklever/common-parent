package com.isoftstone.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableFeignClients
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
public class ServiceBackupMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBackupMongoApplication.class, args);
	}
}
