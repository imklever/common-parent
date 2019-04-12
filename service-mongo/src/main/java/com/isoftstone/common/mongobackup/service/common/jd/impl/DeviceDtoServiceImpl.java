package com.isoftstone.common.mongobackup.service.common.jd.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.domain.mysql.DeviceDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.DeviceDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.DeviceDtoService;

@Service
public class DeviceDtoServiceImpl extends AbstractBaseService<DeviceDto, String> implements
DeviceDtoService {

	@Autowired
	DeviceDtoMapper deviceDtoMapper;
	
	@Override
	protected BaseMapper<DeviceDto, String> getMapper() { 
		return deviceDtoMapper;
	}

	@Override
	public List<DeviceDto> selectAll() {
		return deviceDtoMapper.selectAll();
	}

	@Override
	public List<DeviceDto> selectByTypeId(String typeId) {
		return deviceDtoMapper.selectByTypeId(typeId);
	}

}
