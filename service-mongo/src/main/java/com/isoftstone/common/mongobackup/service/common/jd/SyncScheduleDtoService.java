package com.isoftstone.common.mongobackup.service.common.jd;

import com.isoftstone.common.jd.SyncScheduleDto;
import com.isoftstone.common.mongobackup.service.BaseService;

public interface SyncScheduleDtoService  extends BaseService<SyncScheduleDto, String> {
	int insert(SyncScheduleDto syncScheduleDto);
	void deleteAll();
	SyncScheduleDto selectOne();
}