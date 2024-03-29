package com.isoftstone.common.backup.support;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
}
