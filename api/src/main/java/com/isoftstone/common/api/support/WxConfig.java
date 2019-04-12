package com.isoftstone.common.api.support;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceApacheHttpImpl;
import com.isoftstone.common.ApiApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@EnableAutoConfiguration 
@ComponentScan(basePackageClasses = ApiApplication.class)
public class WxConfig extends WebMvcConfigurationSupport{
	
	@Bean 
	public WxPayService getWxPayService(){ 
	return new WxPayServiceApacheHttpImpl(); 
	} 
	
}
