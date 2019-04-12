package com.isoftstone.common.common.sys.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.common.sys.SysJdbcServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixSysJdbcServiceClient implements SysJdbcServiceClient{
	@Autowired
	SysJdbcServiceClient sysJdbcServiceClient;
	@Override
	@HystrixCommand(fallbackMethod = "saveFallBackCall")
	public SysJdbcDto save(String dtoJson) {
		return sysJdbcServiceClient.save(dtoJson);
	}
	public SysJdbcDto saveFallBackCall(String dtoJson) {
		return null;
	}

}
