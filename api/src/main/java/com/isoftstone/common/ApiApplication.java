package com.isoftstone.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.isoftstone.common.api.AppConfig;



@SpringBootApplication(scanBasePackageClasses= {AppConfig.class},scanBasePackages
= {"com.isoftstone.common","com.isoftstone.common.api","com.isoftstone.common.api.support"})
@EnableEurekaClient
@EnableAutoConfiguration
@EnableHystrix
@EnableFeignClients
@EnableScheduling
@ComponentScan(basePackages = "com.isoftstone.common,com.isoftstone.common.api,com.isoftstone.common.api.support")
public class ApiApplication{

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	

}
