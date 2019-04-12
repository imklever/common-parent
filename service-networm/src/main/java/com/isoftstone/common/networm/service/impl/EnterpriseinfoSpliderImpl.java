package com.isoftstone.common.networm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.networm.service.EnterpriseinfoSpliderService;
import com.isoftstone.common.util.JsonService;

@Service
public class EnterpriseinfoSpliderImpl implements EnterpriseinfoSpliderService{

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	JsonService jsonService;
	
	@Override
	public String getData(String url) {		
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		return responseEntity.getBody();
	}

	@Override
	public List<Map<String,String>> getList(String data) {
	   List<Map<String,String>> list=(List<Map<String, String>>) jsonService.parseObject(data);
	   return list;
	}

	@Override
	public Map<String, Object> getDataMap(String data) {
		return jsonService.parseMap(data);
	}
   
}
