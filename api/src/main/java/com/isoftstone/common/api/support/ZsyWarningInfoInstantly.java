package com.isoftstone.common.api.support;

import com.isoftstone.common.api.support.websocket.WebSocketServer;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ZsyWarningInfoInstantly {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	JsonService jsonService;
	
	public final static long ONE_MINUTE =  60 * 1000;
	public static String dataList = null;
	
	//@Scheduled(fixedRate=ONE_MINUTE)
    public void warningPromptPush(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("system_owned", "OTN");
		
		Map<String, Object> datas = hystrixVisuaSqlExampleClient.getByDataBusinessCode("S06051", jsonService.toJson(param));
		String data1 = jsonService.toJson(datas);
		if(datas!=null) {
			if(!data1.equals(dataList)) {
				dataList = data1;
				sendAllMessage(dataList);
			}else {
				System.out.println("前后数据相同！");
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
