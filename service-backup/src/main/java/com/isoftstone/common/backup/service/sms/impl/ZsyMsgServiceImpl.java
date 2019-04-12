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

import com.isoftstone.common.backup.service.sms.ZsyMsgService;
import com.isoftstone.common.util.JsonService;
@Service
public  class ZsyMsgServiceImpl implements ZsyMsgService{
	@Autowired
	RestTemplate  restTemplate;
	@Autowired
	JsonService jsonService;
	public void test(String appId,String publickKey,String id,String accountCode,String userId,String recNumber,String msgContent) throws  Exception {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
	    HttpHeaders headers = new HttpHeaders();
        requestEntity.add("appId", appId);
        requestEntity.add("password", publickKey);
        requestEntity.add("destIsdn", recNumber);//手机号
        requestEntity.add("content", msgContent);//内容
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestEntity, headers);
        String url="http://msg.petrochina/servlet/httpsubmit";
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getBody());
	}
}
