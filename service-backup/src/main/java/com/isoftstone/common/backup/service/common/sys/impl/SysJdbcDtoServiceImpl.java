package com.isoftstone.common.backup.service.common.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isoftstone.common.backup.mapper.common.sys.SysJdbcDtoMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.common.sys.SysJdbcDtoService;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.mapper.BaseMapper;
@Service
@Transactional
public class SysJdbcDtoServiceImpl extends AbstractBaseService<SysJdbcDto, String>
implements SysJdbcDtoService{
    @Autowired
	SysJdbcDtoMapper sysJdbcDtoMapper;
	@Override
	protected BaseMapper<SysJdbcDto, String> getMapper() {
		return sysJdbcDtoMapper;
	}
	@Override
	public List<SysJdbcDto> findAll() {
		return sysJdbcDtoMapper.findAll();
	}

}
