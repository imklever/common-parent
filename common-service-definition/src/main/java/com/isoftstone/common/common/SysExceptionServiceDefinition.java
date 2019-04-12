package com.isoftstone.common.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface SysExceptionServiceDefinition {
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	int  insert(@RequestParam(value = "sysExceptionLogDto", required = true,defaultValue = "{}")String sysExceptionLogDto);
}
