package com.isoftstone.common.common.sys.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.common.sys.SysJdbcServiceClient;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.common.sys.SysUserServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixSysUserServiceClient implements SysUserServiceClient{
	@Autowired
	SysUserServiceClient sysUserServiceClient;

	@Override
	@HystrixCommand(fallbackMethod = "findByUserPassFallBackCall")
	public SysUserDto findByUserPass(String name, String password) {
		return sysUserServiceClient.findByUserPass(name, password);
	}

	public SysUserDto findByUserPassFallBackCall(String name, String password) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "updateByUserTokenFallBackCall")
	public int updateByUserToken(String name, String token, String overduetime) {
		return sysUserServiceClient.updateByUserToken(name, token, overduetime);
	}
	
	public int updateByUserTokenFallBackCall(String name, String token, String overduetime) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByIdFallBackCall")
	public SysUserDto findById(String id) {
		return sysUserServiceClient.findById(id);
	}
	
	public SysUserDto findByIdFallBackCall(String id) {
		return null;
	}

	@Override
	@HystrixCommand(fallbackMethod = "updateTokenByIdFallBackCall")
	public int updateTokenById(String id,String token,String overduetime) {
		return sysUserServiceClient.updateTokenById(id,token,overduetime);
	}
	
	public int updateTokenByIdFallBackCall(String id,String token,String overduetime) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "updateByPrimaryKeySelectiveFallBackCall")
	public int updateByPrimaryKeySelective(@RequestBody SysUserDto sysUserDto) {
	    return sysUserServiceClient.updateByPrimaryKeySelective(sysUserDto);
	}
	
	public int updateByPrimaryKeySelectiveFallBackCall(SysUserDto sysUserDto) {
		return 0;
	}

	@Override
	@HystrixCommand(fallbackMethod = "findByPhoneFallBackCall")
	public SysUserDto findByPhone(String phone) {
		return sysUserServiceClient.findByPhone(phone);
	}
	
	public SysUserDto findByPhoneFallBackCall(String phone) {
		// TODO Auto-generated method stub
		return null;
	}
}
