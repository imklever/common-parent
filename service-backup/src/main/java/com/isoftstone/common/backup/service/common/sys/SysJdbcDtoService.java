package com.isoftstone.common.backup.service.common.sys;

import java.util.List;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.sys.SysJdbcDto;

public interface SysJdbcDtoService extends BaseService<SysJdbcDto, String>{
	List<SysJdbcDto>findAll();
}
