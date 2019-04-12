package com.isoftstone.common.api.service.interceptor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.interceptor.PublicBusinessService;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
@Service
public class PublicBusinessServiceImpl implements PublicBusinessService{
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	JsonService jsonService;
	@Override
	public boolean checkBusinessCode(String businessCode) {
		boolean flag=false;
		Map<String, Object> map=hystrixVisuaSqlExampleClient.getByDataBusinessCode("S02012", "{}");
		if(map!=null&&map.containsKey("dataList")) {
			List<Map<String,Object>> list=(List<Map<String, Object>>) map.get("dataList");
			//System.out.println(JSONObject.toJSON(list));
			for (Map<String, Object> map2 : list) {
				if(businessCode.equals(map2.get("business_code"))) {
					flag= true;
				    break;			
				}
			}
		}
		return flag;
	}
	@Override
	public boolean checkBusinessCode(String businessCode, String role_id) {
		boolean flag=false;
		Map<String, String>parmsMap=new HashMap<String, String>();
		parmsMap.put("role_id", role_id);
		parmsMap.put("business_code", businessCode);
		Map<String, Object> map=hystrixVisuaSqlExampleClient.getByDataBusinessCode("S03210",jsonService.toJson(parmsMap));
		if(map!=null&&map.containsKey("dataList")) {
			List<Map<String,Object>> list=(List<Map<String, Object>>) map.get("dataList");
			for (Map<String, Object> map2 : list) {
				if(map2.get("count")!=null&&(int)map2.get("count")>0) {
					flag= true;
				    break;	
				}
			}
		}
		return flag;
	}

}
