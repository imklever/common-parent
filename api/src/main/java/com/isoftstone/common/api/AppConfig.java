package com.isoftstone.common.api;

//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.isoftstone.common.ApiApplication;
import com.isoftstone.common.api.support.interceptor.CrossDomainInterceptor;
import com.isoftstone.common.api.support.interceptor.HttpLogInterceptor;
import com.isoftstone.common.api.support.interceptor.PermissionsInterceptor;
import com.isoftstone.common.api.support.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
//@EnableAutoConfiguration 
@ComponentScan(basePackageClasses = ApiApplication.class, useDefaultFilters = true)
@Order(-1)
public class AppConfig extends WebMvcConfigurationSupport {
    @Bean
    public HandlerInterceptor getHttpLogInterceptor() {
        return new HttpLogInterceptor();
    }

    @Bean
    public HandlerInterceptor getCrossDomainInterceptor() {
        return new CrossDomainInterceptor();
    }

    @Bean
    public HandlerInterceptor getPermissionsInterceptor() {
        return new PermissionsInterceptor();
    }

    @Bean
    public HandlerInterceptor getUserInterceptor() {
        return new UserInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getCrossDomainInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getHttpLogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getUserInterceptor()).addPathPatterns("/common/**");
        registry.addInterceptor(getPermissionsInterceptor()).addPathPatterns("/common/**");
        super.addInterceptors(registry);
    }
	
}
