package com.isoftstone.common.backup.mapper.common.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.mapper.BaseMapper;

@Mapper
public interface SysRoleDtoMapper  extends BaseMapper<SysRoleDto, String>{
	List<SysRoleDto> findByUserId(String userId);
}