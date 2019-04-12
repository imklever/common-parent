package com.isoftstone.common.backup.service.caictService;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface CaictService {
	
//获取url
	void syschronize(Map<String, Object> map);

}
