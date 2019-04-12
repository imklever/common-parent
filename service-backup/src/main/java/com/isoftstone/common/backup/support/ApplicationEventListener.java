package com.isoftstone.common.backup.support;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * spring启动容器监听
 * ApplicationEnvironmentPreparedEvent 初始化环境变量 
 * ApplicationPreparedEvent 初始化完成 
 * ContextRefreshedEvent 应用刷新 
 * ApplicationReadyEvent  应用已启动完成 
 * ContextStartedEvent  应用启动，需要在代码动态添加监听器才可捕获
 * ContextStoppedEvent  应用停止 
 * ContextClosedEvent   应用关闭 
 * @author xushiqing
 *
 */
@Component
@Async
public class ApplicationEventListener implements ApplicationListener<ApplicationReadyEvent>{
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		System.out.println("++++++启动完成监听器运行++++++++++");
		SpringUtils.setConfigurableApplicationContext(event.getApplicationContext());
		System.out.println("++++++启动完成监听器运行结束++++++++++");
	}
}
