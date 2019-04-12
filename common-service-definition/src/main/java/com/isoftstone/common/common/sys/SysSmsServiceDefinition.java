package com.isoftstone.common.common.sys;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


public interface SysSmsServiceDefinition {
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	void sendMessage(@RequestParam(value = "name", required = true) Map<String, Object> datas
			);
}
