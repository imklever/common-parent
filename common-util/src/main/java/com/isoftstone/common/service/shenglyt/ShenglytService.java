package com.isoftstone.common.service.shenglyt;


import java.util.List;
import java.util.Map;
	
public interface ShenglytService {
	//构造数据 油井实时数据
	List<Map<String, Object>> getYjScores(Map<String,Object>map);
	//水井实时数据
	List<Map<String, Object>> getSjScores(Map<String,Object>map); 
	//油井功图数据
	List<Map<String, Object>> getGtScores(Map<String,Object>map);
}
