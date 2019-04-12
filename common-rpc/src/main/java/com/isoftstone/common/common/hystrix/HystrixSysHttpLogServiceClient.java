package com.isoftstone.common.common.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.SysHttpLogDto;
import com.isoftstone.common.common.SysHttpLogServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixSysHttpLogServiceClient implements SysHttpLogServiceClient{
	
	@Autowired
	SysHttpLogServiceClient sysHttpLogServiceClient;
	
	@Override
	@HystrixCommand(fallbackMethod = "insertFallBackCall")
	public int insert(String sysHttpLogDto) {		
		return sysHttpLogServiceClient.insert(sysHttpLogDto);
	}

	public int insertFallBackCall(String sysHttpLogDto) {
		return 0;
	}

}
