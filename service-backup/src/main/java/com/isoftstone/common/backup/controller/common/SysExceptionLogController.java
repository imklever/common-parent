package com.isoftstone.common.backup.controller.common;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.backup.service.common.SysExceptionService;
import com.isoftstone.common.common.SysExceptionLogDto;
import com.isoftstone.common.common.SysExceptionServiceDefinition;
import com.isoftstone.common.util.JsonService;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
 	path = ServiceBackConstants.PATH_COMMON_EXCEPTION_LOG)
public class SysExceptionLogController  implements SysExceptionServiceDefinition{

	@Autowired
	JsonService jsonService;
	@Autowired
	SysExceptionService sysExceptionService;
	
	@Override
	public int insert(@RequestParam(value = "sysExceptionLogDto", required = true)String sysExceptionLogDto) {		
		SysExceptionLogDto sysExceptionLog=jsonService.parseObject(sysExceptionLogDto, SysExceptionLogDto.class);		
		return sysExceptionService.insert(sysExceptionLog);
	}
}
