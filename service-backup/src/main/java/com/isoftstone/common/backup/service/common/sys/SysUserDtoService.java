package com.isoftstone.common.backup.service.common.sys;

import java.util.List;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.common.sys.SysUserDto;

public interface SysUserDtoService extends BaseService<SysUserDto, String>{
	List<SysUserDto> findAll();
	
	SysUserDto findByNamePass(String name,String password);	
	
	int updateByUserToken(String name,String token,String overDueTime);
	
	int updateTokenById(String id,String token,String overDueTime);
	
	int updateByPrimaryKeySelective(SysUserDto sysUserDto);
	
	SysUserDto findByPhone(String phone);
}
