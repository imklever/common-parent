package com.isoftstone.common.backup.service.common;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.SysExceptionLogDto;

public interface SysExceptionService extends BaseService<SysExceptionLogDto, String>{
	  int insert(SysExceptionLogDto sysExceptionLogDto);
}
