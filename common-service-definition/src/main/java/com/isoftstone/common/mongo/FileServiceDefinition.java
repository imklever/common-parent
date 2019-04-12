package com.isoftstone.common.mongo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface FileServiceDefinition {
	@RequestMapping(value = "/getFileStream", method = RequestMethod.GET)
	int getFileInputStream(@RequestParam(value = "fileName", required = true,defaultValue = "")String fileName);

}
