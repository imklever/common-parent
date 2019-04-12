package com.isoftstone.common.service.zsy;

import java.util.List;
import java.util.Map;

public interface ZsyService {
	
	//中石油指标查询
	List<Map<String, Object>> selectTarget(Map<String,Object> map);

}
