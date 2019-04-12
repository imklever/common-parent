package com.isoftstone.common.common.sys;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public interface SysJdbcServiceDefinition {
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	SysJdbcDto save(@RequestBody String dtoJson);
}
