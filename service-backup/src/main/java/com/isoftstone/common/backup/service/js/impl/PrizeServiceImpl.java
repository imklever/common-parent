package com.isoftstone.common.backup.service.js.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.backup.service.js.PrizeService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.util.JsonService;
@Service
public class PrizeServiceImpl implements PrizeService{
	String listName="dataList";
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;

	@Autowired
	private JsonService jsonService;
	@Override
	public void prize(Map<String, Object> map) {
		System.out.println("--");
		System.out.println(jsonService.toJson(map));
		if(map != null && map.containsKey(listName)) {
			List<Map<String, Object>> list = 
					(List<Map<String, Object>>) map.get(listName);
			for (Map<String, Object> prize : list) {
				System.out.println("--");
				Map<String, Object> remap=visuaSqlExampleService.getByDataBusinessCode("E03202", jsonService.toJson(prize));
				if(remap!=null) {
					System.out.println(remap);
				}
				
			}
		}
		
	}

}
