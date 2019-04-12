package com.isoftstone.common.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface SysHttpLogServiceDefinition {	
	@RequestMapping(value = "/add")
	int insert(@RequestParam(value = "sysHttpLogDto", required = true,defaultValue = "{}")String sysHttpLogDto);		
}
