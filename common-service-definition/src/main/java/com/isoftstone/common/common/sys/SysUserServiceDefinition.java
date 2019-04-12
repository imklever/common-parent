package com.isoftstone.common.common.sys;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


public interface SysUserServiceDefinition {
	@RequestMapping(value = "/getUser", method = RequestMethod.POST)
	SysUserDto findByUserPass(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "password", required = true) String password);
	
	@RequestMapping(value = "/getUserById", method = RequestMethod.POST)
	SysUserDto findById(@RequestParam(value = "id", required = true) String id);
	
	@RequestMapping(value = "/updateByUserToken", method = RequestMethod.POST)
	int updateByUserToken(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "overduetime", required = true) String overduetime);
	
	@RequestMapping(value = "/updateTokenById", method = RequestMethod.POST)
	int updateTokenById(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "overduetime", required = true) String overduetime);
	
	@RequestMapping(value = "/updateByPrimaryKeySelective", method = RequestMethod.POST)
	int updateByPrimaryKeySelective(@RequestBody SysUserDto sysUserDto);
	
	@RequestMapping(value = "/findByPhone", method = RequestMethod.POST)
	SysUserDto findByPhone(@RequestParam(value = "phone", required = true) String phone);
	
}
