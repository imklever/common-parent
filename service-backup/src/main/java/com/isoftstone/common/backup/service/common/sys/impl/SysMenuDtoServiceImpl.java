package com.isoftstone.common.backup.service.common.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isoftstone.common.backup.mapper.common.sys.SysMenuDtoMapper;
import com.isoftstone.common.backup.mapper.common.sys.SysRoleDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.sys.SysMenuDtoService;
import com.isoftstone.common.backup.service.common.sys.SysRoleDtoService;
import com.isoftstone.common.common.sys.SysMenuDto;
import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.mapper.BaseMapper;

@Service
@Transactional
public class SysMenuDtoServiceImpl extends AbstractBaseService<SysMenuDto, String>
implements SysMenuDtoService{

	@Autowired
	SysMenuDtoMapper sysMenuDtoMapper;
	
	@Override
	protected BaseMapper<SysMenuDto, String> getMapper() {
		return sysMenuDtoMapper;
	}

	@Override
	public List<SysMenuDto> selectByIds(List<String> menuIds) {		
		return sysMenuDtoMapper.findByIds(menuIds);
	}


}
