package com.isoftstone.common.service.zsy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.service.zsy.ZsyService;

@Service
public class ZsyServiceImpl implements ZsyService {

	@Override
	public List<Map<String, Object>> selectTarget(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(map != null && map.containsKey("datalist")) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("datalist");
			System.out.println(maplist.size());
			for (Map<String, Object> map1 : maplist) {
				Set<String> keySet = map1.keySet();
				Map<String, Object> map2 = new HashMap<String, Object>();
				for (String key : keySet) {
					if("pmMeasurementList".equals(key)) {
						String value1 = (String) map1.get("pmMeasurementList");
						//System.out.println("value1:"+value1);
						if(null!=value1) {
							//正则匹配"},{"中间的逗号
							String[] strList = value1.substring(1, value1.length()-1).split("(?<=\\}),(?=\\{)");
							for (String str : strList) {
								//System.out.println("str:"+str);
								JSONObject jsonStr = JSONObject.parseObject(str);
								if("PMP_CLSOPMAX".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP_MAX", jsonStr.getString("value")+jsonStr.getString("unit"));
								}else if("PMP_RPL_MAX".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP_MAX", jsonStr.getString("value")+jsonStr.getString("unit"));
								} 
								if("PMP_CLSOPMIN".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP_MIN", jsonStr.getString("value")+jsonStr.getString("unit"));
								}else if("PMP_RPL_MIN".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP_MIN", jsonStr.getString("value")+jsonStr.getString("unit"));
								}
								if("PMP_PCLSOP".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP", jsonStr.getString("value")+jsonStr.getString("unit"));
								}else if("PMP_RPL".equals(jsonStr.getString("pmParameterName"))) {
									map2.put("PMP", jsonStr.getString("value")+jsonStr.getString("unit"));
								}
							}
						}else {
							map2.put("PMP_MAX", "0dBm");
							map2.put("PMP_MIN", "0dBm");
							map2.put("PMP", "0dBm");
						}
					}else {
						map2.put(key,map1.get(key));
					}
				}
				list.add(map2);
			}
		}
		return list;
	}
	
}
