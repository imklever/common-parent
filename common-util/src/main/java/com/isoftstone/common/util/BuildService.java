package com.isoftstone.common.util;

import java.util.List;
import java.util.Map;


public interface BuildService {

	List<Object> buildTree(List<Map<String, Object>> list, String maps,String outPut);
	
}
