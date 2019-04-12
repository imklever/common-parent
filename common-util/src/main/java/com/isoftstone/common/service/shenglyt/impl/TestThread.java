package com.isoftstone.common.service.shenglyt.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.isoftstone.ai.slytai.AIInterface;
//import com.isoftstone.ai.slytai.APIConstants;
import com.isoftstone.common.util.SqlTemplateReplacement;
@Service
public class TestThread  extends Thread{
     private List<Map<String, Object>> list;
     private String apiConstants;
/*	 public TestThread(String Key, List<Map<String, Object>> subList) {
	 this.list= subList;
	 this.apiConstants= Key;
	}*/
     public List<Map<String, Object>> getList() {
 		return list;
 	}
 	public void setList(List<Map<String, Object>> list) {
 		this.list = list;
 	}
 	public String getApiConstants() {
 		return apiConstants;
 	}
 	public void setApiConstants(String apiConstants) {
 		this.apiConstants = apiConstants;
 	}
	@Override
    public void run() {
		
     }
}
