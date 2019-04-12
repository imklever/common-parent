package com.isoftstone.common.mongobackup.service.common.jd.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; 

import com.isoftstone.common.jd.SyncScheduleDto;
import com.isoftstone.common.mongobackup.mapper.BaseMapper;
import com.isoftstone.common.mongobackup.mapper.SyncScheduleDtoMapper;
import com.isoftstone.common.mongobackup.service.AbstractBaseService;
import com.isoftstone.common.mongobackup.service.common.jd.SyncScheduleDtoService;
import com.isoftstone.common.util.JsonService;

@Service
public class SyncScheduleDtoServiceImpl extends AbstractBaseService<SyncScheduleDto, String> 
implements SyncScheduleDtoService {

	@Autowired
	SyncScheduleDtoMapper syncScheduleDtoMapper;
	@Autowired
	JsonService  jsonService;
	
	@Override
	protected BaseMapper<SyncScheduleDto, String> getMapper() {		 
		return syncScheduleDtoMapper;
	}
	 
	@Override
	public int insert(SyncScheduleDto syncScheduleDto) {	
		 return syncScheduleDtoMapper.insert(syncScheduleDto);
	}

	@Override
	public void deleteAll() {
		syncScheduleDtoMapper.deleteAll();
	}

	@Override
	public SyncScheduleDto selectOne() {
		return syncScheduleDtoMapper.selectOne();
	}
 
}
