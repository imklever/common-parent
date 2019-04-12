package com.isoftstone.common.backup.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isoftstone.common.backup.mapper.common.SysHttpLogDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.SysHttpLogService;
import com.isoftstone.common.common.SysHttpLogDto;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.mapper.BaseMapper;
@Service
@Transactional
public class SysHttpLogServiceImpl extends AbstractBaseService<SysHttpLogDto, String> 
implements SysHttpLogService{
	
	@Autowired
	SysHttpLogDtoMapper sysHttpLogDtoMapper;
	
	@Override
	protected BaseMapper<SysHttpLogDto, String> getMapper() {
		return sysHttpLogDtoMapper;
	}
	
	@Override
	public int insert(SysHttpLogDto sysHttpLogDto) {		
		return sysHttpLogDtoMapper.insert(sysHttpLogDto);
	}


}
