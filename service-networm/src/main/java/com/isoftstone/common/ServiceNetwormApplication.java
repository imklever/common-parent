package com.isoftstone.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableFeignClients
//@EnableAutoConfiguration
//@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.isoftstone.common")
public class ServiceNetwormApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceNetwormApplication.class, args);
	}
}
