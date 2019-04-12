package com.isoftstone.common.common.hystrix;

import org.springframework.beans.factory.annotation.Autowired;

import com.isoftstone.common.common.SysExceptionLogDto;
import com.isoftstone.common.common.SysExceptionServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

public class HystrixSysExceptionServiceClient implements
		SysExceptionServiceClient {

	@Autowired
	SysExceptionServiceClient sysExceptionServiceClient;
	
	@Override
	@HystrixCommand(fallbackMethod = "insertFallBackCall")
	public int insert(String sysExceptionLogDto) {		
		return sysExceptionServiceClient.insert(sysExceptionLogDto);
	}
	 
	public int insertFallBackCall(String sysExceptionLogDto) {
		// TODO Auto-generated method stub
		return 0;
	}
 

}
