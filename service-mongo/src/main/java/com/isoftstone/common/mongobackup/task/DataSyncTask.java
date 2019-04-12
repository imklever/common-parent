package com.isoftstone.common.mongobackup.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.isoftstone.common.mongobackup.constant.Constant;
import com.isoftstone.common.mongobackup.service.common.jd.SyncTaskService;

@Component
public class DataSyncTask implements SchedulingConfigurer {

	@Value("${jd.data.sync:false}")
	boolean jd_sync_data;
	@Value("${jd.data.cron:0 0/5 * * * ?}")
	String cron;
	
	@Autowired
	SyncTaskService syncTaskService;
	
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
				if(Constant.SYNC){
					synchronized(this){
						Long startTime =System.currentTimeMillis();
						System.out.println("开始执行数据同步,时间为:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(startTime)));				
						//京东数据同步接口
						syncTaskService.sync();
						Long endTime = System.currentTimeMillis();
						System.out.println("同步数据成功。时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(endTime)));
						System.out.println("同步用时："+String.valueOf(endTime-startTime)+" 毫秒。");
					}
				}
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
