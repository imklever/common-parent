package com.isoftstone.common.common.sys.hystrix;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.common.sys.SysRoleServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixSysRoleServiceClient implements SysRoleServiceClient{
	@Autowired
	SysRoleServiceClient SysRoleServiceClient;
	
	@Override
	@HystrixCommand(fallbackMethod = "findByUserIdFallBackCall")
	public List<SysRoleDto> findByUserId(String userId) {
		return SysRoleServiceClient.findByUserId(userId);
	}
	
	public List<SysRoleDto> findByUserIdFallBackCall(String userId) {
		return null;
	}
}
