package com.isoftstone.common.backup.support;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.isoftstone.common.BaseDatabaseInfo;




public class BaseSpringUtils {
	
	public static ServletContext getServletContext(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext(); 
		return webApplicationContext.getServletContext();
	}
	
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}
	
	public static HttpSession getSession(){
		return getRequest().getSession(true);
	}
	
	private static final String SESSION_KEY_PROGRESS_MESS = "SESSION_KEY_PROGRESS_MESS";

	
	public static <T> Collection<T> getBeansOfType(Class<T> clz) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		if(wac == null)
			return Collections.EMPTY_LIST;
		Map result = wac.getBeansOfType(clz);
		if(result == null)
			return Collections.EMPTY_LIST;
		return result.values();
	}
	
	public static <T> T getSingleBeanOfType(Class<T> clz) {
		Collection<T> list = BaseSpringUtils.getBeansOfType(clz);
		if(list == null || list.isEmpty())
			return null;
		return list.iterator().next();
	}
	
	public static <T> T getSingleBeanByName(String name) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return (T)wac.getBean(name);
	}
	
	public static void createJdbcTemplate(BaseDatabaseInfo baseDatabaseInfo){
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ContextLoader.getCurrentWebApplicationContext()  
                .getAutowireCapableBeanFactory();
		String dataSourceKey = baseDatabaseInfo.getId()+"_dataSource";
		String jdbcTemplateKey = baseDatabaseInfo.getId()+"_jdbcTemplate";
		//创建dataSource
		BeanDefinitionBuilder dataSourceBean = BeanDefinitionBuilder.rootBeanDefinition("org.apache.commons.dbcp.BasicDataSource");
		dataSourceBean.getBeanDefinition().setAttribute("id", dataSourceKey);  
		dataSourceBean.addPropertyValue("driverClassName", baseDatabaseInfo.getDriver());  
		dataSourceBean.addPropertyValue("url", baseDatabaseInfo.getUrl());  
		dataSourceBean.addPropertyValue("username", baseDatabaseInfo.getUsername());  
		dataSourceBean.addPropertyValue("password", baseDatabaseInfo.getPassword());  
		dataSourceBean.addPropertyValue("timeBetweenEvictionRunsMillis", 3600000);  
		dataSourceBean.addPropertyValue("minEvictableIdleTimeMillis", 3600000); 
		dataSourceBean.addPropertyValue("validationQuery", "SELECT 1");  
		dataSourceBean.addPropertyValue("testOnBorrow", true);
        //  注册bean  
        acf.registerBeanDefinition(dataSourceKey, dataSourceBean.getBeanDefinition()); 
        
        BeanDefinitionBuilder jdbcTemplateBean = BeanDefinitionBuilder.rootBeanDefinition("org.springframework.jdbc.core.JdbcTemplate");
        jdbcTemplateBean.getBeanDefinition().setAttribute("id", jdbcTemplateKey);  
        jdbcTemplateBean.addPropertyValue("dataSource", getSingleBeanByName(dataSourceKey));   
        //  注册bean  
        acf.registerBeanDefinition(jdbcTemplateKey, jdbcTemplateBean.getBeanDefinition());
	}
	
}
