package com.isoftstone.common.backup.service.sms.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.backup.service.sms.JDMsgService;
import com.isoftstone.common.backup.support.Base64Utils;
import com.isoftstone.common.backup.support.Md5Utils;
import com.isoftstone.common.backup.support.RSAUtils;
import com.isoftstone.common.util.JsonService;
@Service
public  class JDMsgServiceImpl implements JDMsgService{
	@Autowired
	RestTemplate  restTemplate;
	@Autowired
	JsonService jsonService;
	public void test(String appId,String publickKey,String id,String accountCode,String userId,String recNumber,String msgContent) throws  Exception {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
	     HttpHeaders headers = new HttpHeaders();
	     HashMap<String, Object> header = new HashMap<String, Object>();
	      String serviceName = "smsMulti";
	      header.put("serviceName", serviceName);//-----写死
	      header.put("version", "1.0");//------写死
	      header.put("clientMessageId", serviceName + id);//---短信日志表中id
	      header.put("timestamp", System.currentTimeMillis());//------写死
	      
	      HashMap<String, Object> body = new HashMap<String, Object>();
	      body.put("clientMessageId", serviceName + id);//TODO 需要修改 //---自己ID
	      body.put("accountCode", accountCode);//TODO 需要修改--------京东账号信息
	      body.put("userId", userId);//TODO 需要修改--------京东账号信息
	      body.put("recNumber", recNumber);//TODO 需要修改    手机号 
	      body.put("msgContent", msgContent);//TODO 需要修改        内容
	      HashMap<String, Object> map = new HashMap<String, Object>();
	      map.put("header", header);
	      map.put("body", body);
	      byte[] encodeDate = RSAUtils.encryptByPublicKey(
	    		  jsonService.toJson(map)
	    		  .getBytes("utf-8"), publickKey);
	     String bizParam = Base64Utils.encode(encodeDate);
      //签名校验
       String sign = Md5Utils.md5(jsonService.toJson(map));
        requestEntity.add("appId", appId);
        requestEntity.add("sign", sign);
        requestEntity.add("bizParam", bizParam);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestEntity, headers);

        String url="https://commcso.jd.com/open/api/invoke.do";
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getBody());
	}
}
