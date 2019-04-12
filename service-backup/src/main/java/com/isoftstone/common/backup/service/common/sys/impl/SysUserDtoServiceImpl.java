package com.isoftstone.common.backup.service.common.sys.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isoftstone.common.backup.mapper.common.sys.SysJdbcDtoMapper;
import com.isoftstone.common.backup.mapper.common.sys.SysUserDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.sys.SysJdbcDtoService;
import com.isoftstone.common.backup.service.common.sys.SysUserDtoService;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.util.CommUtil;
@Service
@Transactional
public class SysUserDtoServiceImpl extends AbstractBaseService<SysUserDto, String>
implements SysUserDtoService{

	@Autowired
    SysUserDtoMapper sysUserDtoMapper;
	@Override
	protected BaseMapper<SysUserDto, String> getMapper() {
		return sysUserDtoMapper;
	}
	@Override
	public List<SysUserDto> findAll() {
		return sysUserDtoMapper.findAll();
	}
	@Override
	public SysUserDto findByNamePass(String name, String password) {

		Map<String, String> paramMap=new HashMap<String,String>();
		paramMap.put("name",name);
		
		//password=CommUtil.getMD5(password);
		paramMap.put("password",password); 
		
	   return sysUserDtoMapper.findByNamePass(paramMap);
	}
	@Override
	public int updateByUserToken(String name,String token,String overDueTime) {	
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("name",name);
		paramMap.put("token",token);
		paramMap.put("overduetime",overDueTime);
		return sysUserDtoMapper.updateByUserToken(paramMap);
	}
	@Override
	public int updateTokenById(String id,String token,String overDueTime) {
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("id",id);
		paramMap.put("token",token);
		paramMap.put("overduetime",overDueTime);
		return sysUserDtoMapper.updateTokenById(paramMap);
	}
	@Override
	public int updateByPrimaryKeySelective(SysUserDto sysUserDto) {
		return sysUserDtoMapper.updateByPrimaryKeySelective(sysUserDto);
	}
	@Override
	public SysUserDto findByPhone(String phone) {
		return sysUserDtoMapper.findByPhone(phone);
	}
}
