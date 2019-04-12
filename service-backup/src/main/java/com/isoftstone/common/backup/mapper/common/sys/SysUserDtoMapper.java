package com.isoftstone.common.backup.mapper.common.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.mapper.BaseMapper;
@Mapper
public interface SysUserDtoMapper extends BaseMapper<SysUserDto, String>{
	
	List<SysUserDto> findAll();
	
	SysUserDto findByNamePass(Map<?, ?> paramMap); 
	
	int updateByUserToken(Map<?, ?> paramMap);
	
	int updateTokenById(Map<?, ?> paramMap);
	SysUserDto findByPhone(String phone);
}