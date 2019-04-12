package com.isoftstone.common.backup.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.backup.mapper.common.SysExceptionDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.SysExceptionService;
import com.isoftstone.common.common.SysExceptionLogDto;
import com.isoftstone.common.mapper.BaseMapper;

@Service
public class SysExceptionServiceImpl extends AbstractBaseService<SysExceptionLogDto, String>  implements SysExceptionService {

	@Autowired
	SysExceptionDtoMapper sysExceptionDtoMapper;
	
	@Override
	protected BaseMapper<SysExceptionLogDto, String> getMapper() {
		return sysExceptionDtoMapper;
	}
	
	@Override
	public int insert(SysExceptionLogDto sysExceptionDto) {
		
		return sysExceptionDtoMapper.insert(sysExceptionDto);
	}

}
