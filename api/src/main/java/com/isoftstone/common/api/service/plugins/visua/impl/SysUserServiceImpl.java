package com.isoftstone.common.api.service.plugins.visua.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.plugins.visua.SysUserService;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.common.sys.SysUserServiceClient;
@Service
public class SysUserServiceImpl implements SysUserService{

	@Autowired
	SysUserServiceClient sysUserServiceClient;
	
	@Override
	public SysUserDto findByUserPass(String name, String password) {	
		return sysUserServiceClient.findByUserPass(name, password);
	}
}
