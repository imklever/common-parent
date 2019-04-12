package com.isoftstone.common.mongobackup.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.common.constant.MongoServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.mongobackup.constant.Constant;
import com.isoftstone.common.mongobackup.service.common.jd.SyncTaskService;
import com.isoftstone.common.mongobackup.task.DataSyncTask;

@RefreshScope
@RestController
@RequestMapping("/data")
public class SyncDataController {
	@Autowired
	SyncTaskService syncTaskService;
	@Autowired
	DataSyncTask dataSyncTask;
	
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	public String syncData(@RequestParam(value = "start", required = true)String start,
			@RequestParam(value = "end", required = true)String end){		
		return sync(start,end);
	}
	
	public String sync(String startDateStr,String endDateStr) {
		 
		String msg="";
		Long startTime =System.currentTimeMillis();
		System.out.println("开始执行数据同步,时间为:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(startTime)));	
	 
		/*
		String startDateStr = "2019-01-19 00:00:00";
		String endDateStr = "2019-01-19 23:59:59";	
		*/
		Date startDate=null;		 
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			
			msg+="开始日期格式不正确，";
		}
		 
		Date endDate=null;		 
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			msg+="结束日期格式不正确，";
		}	 
		try{
			Constant.SYNC=false;
			syncTaskService.sync(startDate,endDate);
			Constant.SYNC=true;
			msg+="数据同步成功。";
		}catch(Exception e){
			msg+=" 同步失败，请联系管理员。";
			Constant.SYNC=true;
		}
		
		
		Long endTime = System.currentTimeMillis();
		System.out.println("同步数据成功。时间为："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(new Date(endTime)));
		System.out.println("同步用时："+String.valueOf(endTime-startTime)+" 毫秒。");
		
		return msg;
	}
}
