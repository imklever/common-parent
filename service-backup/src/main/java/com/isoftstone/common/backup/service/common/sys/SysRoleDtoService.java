package com.isoftstone.common.backup.service.common.sys;

import java.util.List;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.sys.SysRoleDto;

public interface SysRoleDtoService extends BaseService<SysRoleDto, String>{
	List<SysRoleDto> findByUserId(String userId);
}
