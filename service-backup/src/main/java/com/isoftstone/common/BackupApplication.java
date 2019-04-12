package com.isoftstone.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.isoftstone.common.backup.DataSourceConfigurer;


@ComponentScan(basePackages = "com.isoftstone.common,com.isoftstone.common.backup,com.isoftstone.common.service.common")
@SpringBootApplication(exclude = {
		  DataSourceAutoConfiguration.class,
		  //RabbitAutoConfiguration.class,
		  MongoAutoConfiguration.class//,
		  //EmbeddedMongoAutoConfiguration.class
		  //,PageHelperAutoConfiguration.class
		},scanBasePackageClasses= {DataSourceConfigurer.class})
//@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients
public class BackupApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackupApplication.class, args);
	}
}
