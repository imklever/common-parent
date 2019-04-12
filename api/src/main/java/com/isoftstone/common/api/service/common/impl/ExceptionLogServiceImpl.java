package com.isoftstone.common.api.service.common.impl;

import com.isoftstone.common.api.service.common.ExceptionLogService;
import com.isoftstone.common.common.SysExceptionLogDto;
import com.isoftstone.common.common.SysExceptionServiceClient;
import com.isoftstone.common.util.Exceptions;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExceptionLogServiceImpl implements ExceptionLogService{
	@Autowired
    JsonService jsonService;

	@Autowired
	SysExceptionServiceClient SysExceptionServiceClient;
	
	@Override
	public void log(Throwable throwable) {
		  SysExceptionLogDto exceptionLog = new SysExceptionLogDto();
		    exceptionLog.setId(UUID.randomUUID().toString());
	        exceptionLog.setContent("");
	            
	        SysExceptionServiceClient.insert(jsonService.toJson(exceptionLog));
	}

	@Override
	public void log(Throwable throwable, Object context) { 
		    SysExceptionLogDto exceptionLog = new SysExceptionLogDto();
		    exceptionLog.setId(UUID.randomUUID().toString());
		    
	        exceptionLog.setContent(Exceptions.getStackTraceAsString(throwable));
	        if (context instanceof String) {
	            exceptionLog.setContext(jsonService.toJson("{info:'" + context + "'}"));
	        } else {
	            exceptionLog.setContext(jsonService.toJson(context));
	        }
	      
	        SysExceptionServiceClient.insert(jsonService.toJson(exceptionLog));
	}


}
