package com.isoftstone.common.api.support;

import com.isoftstone.common.api.support.websocket.WebSocketServer;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(2)
public class ZsyWarningPromptPush {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	JsonService jsonService;
	
	public final static long ONE_SECOND = 1000;
	
	//@Scheduled(fixedRate=ONE_SECOND)
    public void checkWarningTaskDoOrNot(){
		
		String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println(date);
		Map<String, Object> datas=hystrixVisuaSqlExampleClient.getByDataBusinessCode("S06056", "{}");
		if(datas!=null && datas.containsKey("datalist")) {
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> dataMap = list.get(i);
				//运维处理、发送消息后把告警相关信息推送出去
				Map<String, Object> param = new HashMap<String, Object>();
				String id=(String) dataMap.get("id");
				String is_push=dataMap.get("is_push").toString();
				param.put("id", id);
				if(is_push.equals("0")) {
					dataMap.put("tag_type", 1);
					sendAllMessage(jsonService.toJson(dataMap));
					Map<String, Object> map=hystrixVisuaSqlExampleClient.getByDataBusinessCode("U06056", 
							jsonService.toJson(param));
					if(map.containsKey(ErrorCodeConstants.HASH_ERR)) {
		        		System.out.println(map.get(ErrorCodeConstants.HASH_ERR).toString());
		        	}
				}
				//运维处理、发送消息五分钟后没人处理时把告警相关信息推送出去
				String limit_time=(String) dataMap.get("limit_time");
				String handle_status=(String) dataMap.get("handle_status");
				if(date.equals(limit_time) && handle_status.equals("未处理")) {
					dataMap.put("tag_type", 2);
					sendAllMessage(jsonService.toJson(dataMap));
				}
			}
		}
		
    }
	
	public Object sendAllMessage(String message) {
		try {
			WebSocketServer.BroadCastInfo(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return APIResult.createSuccess();
	}

}
