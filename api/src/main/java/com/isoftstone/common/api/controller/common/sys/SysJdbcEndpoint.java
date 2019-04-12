package com.isoftstone.common.api.controller.common.sys;

import com.isoftstone.common.api.service.plugins.visua.SysJdbcService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.common.sys.SysJdbcDto;
import com.isoftstone.common.common.sys.hystrix.HystrixSysJdbcServiceClient;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMapperUrlConstants.SYS_JDBC)
public class SysJdbcEndpoint {
	@Autowired
	HystrixSysJdbcServiceClient hystrixSysJdbcServiceClient;
	@Autowired
	SysJdbcService sysJdbcService;
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public Object save(@RequestParam(value = "params", required = true) String dtoJson) {
		sysJdbcService.verifyConnection(dtoJson);
		SysJdbcDto result=hystrixSysJdbcServiceClient.save(dtoJson);
		return APIResult.createSuccess(result);
	}
	@RequestMapping(value = "/verify", method = { RequestMethod.POST, RequestMethod.GET })
	public Object verify(@RequestParam(value = "params", required = true) String dtoJson) {
		sysJdbcService.verifyConnection(dtoJson);
		return APIResult.createSuccess(true);
	}
}
