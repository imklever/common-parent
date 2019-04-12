package com.isoftstone.common.backup.controller.common.sys;

import java.util.List;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.backup.service.common.sys.SysRoleDtoService;
import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.common.sys.SysRoleServiceDefinition;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
 	path = ServiceBackConstants.PATH_SYS_ROLE)
public class SysRoleController implements SysRoleServiceDefinition{
	@Autowired
	SysRoleDtoService sysRoleDtoService;

	@Override
	public List<SysRoleDto> findByUserId(String userId) {
		return sysRoleDtoService.findByUserId(userId);
	}
}
