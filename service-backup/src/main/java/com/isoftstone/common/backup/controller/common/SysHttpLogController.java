package com.isoftstone.common.backup.controller.common;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.backup.service.common.SysHttpLogService;
import com.isoftstone.common.common.SysHttpLogDto;
import com.isoftstone.common.common.SysHttpLogServiceDefinition;
import com.isoftstone.common.util.JsonService;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE,
 	path = ServiceBackConstants.PATH_COMMON_HTTP_LOG)
public class SysHttpLogController  implements SysHttpLogServiceDefinition{

	@Autowired
	JsonService jsonService;
	@Autowired
	SysHttpLogService sysHttpLogService;
	
	@Override
	public int insert(@RequestParam(value = "sysHttpLogDto", required = true)String sysHttpLogDto) {
		
		SysHttpLogDto sysHttpLog=jsonService.parseObject(sysHttpLogDto, SysHttpLogDto.class);		
		return sysHttpLogService.insert(sysHttpLog);
	}

}
