package com.isoftstone.common.common.sys;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface SysRoleServiceDefinition {
	@RequestMapping(value = "/getRoleByUserId", method = RequestMethod.POST)
	List<SysRoleDto> findByUserId(@RequestParam(value = "userId", required = true) String userId);
}

