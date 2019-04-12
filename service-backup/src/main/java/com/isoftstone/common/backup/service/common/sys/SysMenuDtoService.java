package com.isoftstone.common.backup.service.common.sys;

import java.util.List;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.sys.SysMenuDto;
import com.isoftstone.common.common.sys.SysRoleDto;

public interface SysMenuDtoService extends BaseService<SysMenuDto, String>{
	List<SysMenuDto> selectByIds(List<String> menuIds);
}
