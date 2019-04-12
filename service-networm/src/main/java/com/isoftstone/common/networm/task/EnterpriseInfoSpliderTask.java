package com.isoftstone.common.networm.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service 
public class EnterpriseInfoSpliderTask implements SchedulingConfigurer {

	@Value("${xty.data.sync:false}")
	boolean jd_sync_data;
	@Value("${xty.data.cron:0 0/5 * * * ?}")
	String cron;
 
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		if (jd_sync_data) {				
			scheduledTaskRegistrar.addTriggerTask(doTask(), getTrigger());
		}
	}

	private Runnable doTask() {
		return new Runnable() {
			@Override
			public void run() {
				Long startTime =System.currentTimeMillis();
				System.out.println("开始执行数据同步,时间为:"+ new Date(startTime));
				
				Long endTime = System.currentTimeMillis();
				System.out.println("同步数据成功。时间为："+new Date(endTime));
				System.out.println("同步用时："+String.valueOf(endTime-startTime));
			}
		};
	}

	private Trigger getTrigger() {
		return new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// 触发器
				CronTrigger trigger = new CronTrigger(getCron());
				return trigger.nextExecutionTime(triggerContext);
			}
		};
	}

	public String getCron() {
		return cron;
	}
   
}
