package com.isoftstone.common.backup.support;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.isoftstone.common.BaseDatabaseInfo;
import com.isoftstone.common.util.SpringUtil;


/**
 * 静态获取web 变量 和  Spring管理的bean
 * @author lenovo
 *
 */
@Service
public class SpringUtils {
	private static ConfigurableApplicationContext configurableApplicationContext;
	
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
	/**
	 * 放置进度信息到session中 方便客户端获取
	 * @param all 要执行的总条数 或 总数 如 100
	 * @param now 现在执行到的条数 如 40
	 * @param text 描述信息，如 删除***成功
	 * 
	 * 最终呈现给用户的是【 40% 删除***成功】
	 */
	public static void setProgressToSession(Integer all, Integer now, String text){
		//计算百分比
		Double baifen = now/Double.valueOf(all+"") * 100;
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
		String num = df.format(baifen) + "%";
		getSession().setAttribute(SESSION_KEY_PROGRESS_MESS, num + " " + text);
	}
	public static void setProgressToSession(long all, long now, String text){
		//计算百分比
		Double baifen = now/Double.valueOf(all+"") * 100;
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
		String num = df.format(baifen) + "%";
		getSession().setAttribute(SESSION_KEY_PROGRESS_MESS, num + " " + text);
	}
	/**
	 * 删除进度信息
	 */
	public static void removeProgressInSession(){
		getSession().removeAttribute(SESSION_KEY_PROGRESS_MESS);
	}
	/**
	 * 获得进度信息
	 */
	public static String getProgressInSession(){
		Object mess = getSession().getAttribute(SESSION_KEY_PROGRESS_MESS);
		if(mess != null){
			return mess.toString();
		}
		return "";
	}
	
	/**
	 * 获得当前用户
	 * @return
	 */
/*	public static BaseUser getCurrentUser(){
		return (BaseUser)getSession().getAttribute(Constants.SESSION_KEY_USER);
	}*/
	
	public static <T> Collection<T> getBeansOfType(Class<T> clz) {
//		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		if(configurableApplicationContext == null)
			return Collections.EMPTY_LIST;
		Map result = configurableApplicationContext.getBeansOfType(clz);
		if(result == null)
			return Collections.EMPTY_LIST;
		return result.values();
	}
	
	public static <T> T getSingleBeanOfType(Class<T> clz) {
		Collection<T> list = SpringUtils.getBeansOfType(clz);
		if(list == null || list.isEmpty())
			return null;
		return list.iterator().next();
	}
	
	public static <T> T getSingleBeanByName(String name) {
		return (T)configurableApplicationContext.getBean(name);
	}
	
	public static BeanDefinitionBuilder createJdbcTemplate(BaseDatabaseInfo baseDatabaseInfo){
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory)SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
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
		dataSourceBean.addPropertyValue("validationQuery", "SELECT 1 from dual");  
		dataSourceBean.addPropertyValue("testOnBorrow", true);
        //  注册bean  
        acf.registerBeanDefinition(dataSourceKey, dataSourceBean.getBeanDefinition()); 
        
        BeanDefinitionBuilder jdbcTemplateBean = BeanDefinitionBuilder.rootBeanDefinition("org.springframework.jdbc.core.JdbcTemplate");
        jdbcTemplateBean.getBeanDefinition().setAttribute("id", jdbcTemplateKey);  
        jdbcTemplateBean.addPropertyValue("dataSource", getSingleBeanByName(dataSourceKey));   
        //  注册bean  
        acf.registerBeanDefinition(jdbcTemplateKey, jdbcTemplateBean.getBeanDefinition());
		return dataSourceBean;
	}

	public static ConfigurableApplicationContext getConfigurableApplicationContext() {
		return configurableApplicationContext;
	}

	public static void setConfigurableApplicationContext(ConfigurableApplicationContext configurableApplicationContext) {
		SpringUtils.configurableApplicationContext = configurableApplicationContext;
	}

	public static String getSessionKeyProgressMess() {
		return SESSION_KEY_PROGRESS_MESS;
	}
	

	}
