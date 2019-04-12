package com.isoftstone.common.mongobackup.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import com.isoftstone.common.mongobackup.service.common.jd.SyncTaskService;

@SpringBootConfiguration
public class SyncOneDayTask {

	@Autowired
	static SyncTaskService syncTaskService;
	
	public static void main(String[] args) {
		 
		Long startTime =System.currentTimeMillis();
		System.out.println("开始执行数据同步,时间为:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(startTime)));	
	 
		/*String startTimeStr=args[0];
		String endTimeStr=args[1];	*/
		String startDateStr = "2019-01-19 00:00:00";
		String endDateStr = "2019-01-19 23:59:59";	
		
		Date startDate=null;		 
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		Date endDate=null;		 
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}	 
		
		//syncTaskService.sync(startDate,endDate);
		
		Long endTime = System.currentTimeMillis();
		System.out.println("同步数据成功。时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(endTime)));
		System.out.println("同步用时："+String.valueOf(endTime-startTime)+" 毫秒。");

	}

}
