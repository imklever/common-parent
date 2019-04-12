package com.isoftstone.common.service.ai.impl;


import com.alibaba.fastjson.JSON;
import com.isoftstone.common.service.ai.BaiDuAiService;
import com.isoftstone.common.util.TransApi;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

//import com.isoftstone.common.service.ai.impl.BaiDuBaseClient;
@Service
public class BaiDuAiServiceImpl implements BaiDuAiService{
	@Override
	public Object freeImageOCR(Map<String, Object> map) {
		String appId=null;
		String apiKey=null;
		String secretKey=null;
		String path=null;
		if(map != null && map.containsKey("dataList")) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) map
					.get("dataList");
			for (Map<String, Object> aa : list) {
				 appId=(String)aa.get("APP_ID");
				 apiKey=(String)aa.get("API_KEY");
				 secretKey=(String)aa.get("SECRET_KEY");
				 path=(String)aa.get("URL");
			}
		}
		 // 初始化一个AipOcr
		BaiDuBaseClient sample= new BaiDuBaseClient(appId, apiKey, secretKey);
        sample.setConnectionTimeoutInMillis(2000);
        sample.setSocketTimeoutInMillis(60000);
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");
        // 调用接口\
        JSONObject res = sample.handwriting(path, options);  
        
		return res.toString();
	}

	@Override
	public List<Map<String, Object>> getTransResult(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(map != null && map.containsKey("dataList")) {
			List<Map<String, Object>> maplist = (List<Map<String, Object>>) map
					.get("dataList");
			System.out.println(maplist.size());
			Map<String, Object> transResultmap = new HashMap<String, Object>();
			for (Map<String, Object> newmap : maplist) {
				Set<String> keySet = newmap.keySet();
				Map<String, Object> map2 = new HashMap<String, Object>();
					for (String key : keySet) {
						String value = newmap.get(key).toString();
						if(null!=value) {
//							if(value.length()>0&&!"id".equals(key)&&!"deal_number".equals(key)&&!"acquiror_country_code".equals(key)&&!"target_country_code".equals(key)&&!"announced_date".equals(key)&&!"last_deal_status_date".equals(key)&&!"deal_value_thEUR".equals(key)) {
							if(value.length()>0&&!"id".equals(key)&&!"pid".equals(key)&&!"year".equals(key)&&!"trade_flow".equals(key)&&!"commodity_code".equals(key)&&!"price".equals(key)&&!"update_state".equals(key)) {
								String transResult="";
								if(transResultmap.containsKey(value)) {
									 transResult = transResultmap.get(value).toString();
								}else {
									 transResult = transResult(value);
									 transResultmap.put(value, transResult);
								}
								map2.put(key,transResult);
							}else {
								map2.put(key,value);
							}
						}else {
							map2.put(key,value);
						}
					}
					list.add(map2);
			}
		}
		System.out.println("list的长度为"+list.size());
		return list;
	}
	
	public String transResult(String value) {
		String result=null;
		try {
			TransApi api = new TransApi("20181029000226688", "pHD0nyU79cnrOVceOXqU");
			String transResult = api.getTransResult(value, "en", "zh");
	        transResult=transResult.replace("[", "");
	        transResult=transResult.replace("]", "");
	        Map maps = (Map)JSON.parse(transResult);
	        if(maps!=null) {
	        	Map object =(Map) maps.get("trans_result");
		        if(object!=null) {
		        result= (String) object.get("dst");
		        }else {
		        	result="";
		        }
	        }else {
	        	result="";
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/*
	 * english : china
	 * chinese : 中国
	 */
	
	@Override
	public List<Map<String, Object>> transColumn(Map<String, Object> map) {
		if(map != null && map.containsKey("dataList")) {
			//数据list
			List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("dataList");
			System.out.println(maplist.size());
			
			for(Map<String, Object> newmap : maplist) {
				String english = (String)newmap.get("english");
				if(english !=null && english.length() > 0) {
					String chinese = transResult(english);
					newmap.put("chinese", chinese);
				}
			}
			return maplist;
		}
		return new ArrayList();
	}
	
	
}
