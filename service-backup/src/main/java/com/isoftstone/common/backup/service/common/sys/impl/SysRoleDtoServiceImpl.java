package com.isoftstone.common.backup.service.common.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isoftstone.common.backup.mapper.common.sys.SysRoleDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.sys.SysRoleDtoService;
import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.mapper.BaseMapper;

@Service
@Transactional
public class SysRoleDtoServiceImpl extends AbstractBaseService<SysRoleDto, String>
implements SysRoleDtoService{

	@Autowired
	SysRoleDtoMapper sysRoleDtoMapper;
	
	@Override
	protected BaseMapper<SysRoleDto, String> getMapper() {
		return sysRoleDtoMapper;
	}

	@Override
	public List<SysRoleDto> findByUserId(String userId) {
		return sysRoleDtoMapper.findByUserId(userId);
	}

}
