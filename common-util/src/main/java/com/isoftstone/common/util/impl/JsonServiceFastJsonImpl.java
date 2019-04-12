package com.isoftstone.common.util.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.util.JsonService;

@Service
public class JsonServiceFastJsonImpl implements JsonService{

	 @Override
	    public String toJson(Object object) {
	        return JSONObject.toJSON(object).toString();
	    }

	    @Override
	    public Object parseObject(String jsonString) {
	        return JSONObject.parse(jsonString);
	    }

	    @Override
	    public <T> T parseObject(String jsonString, Class<T> clazz) {
	        return JSONObject.parseObject(jsonString, clazz);
	    }

	    @Override
	    public <T> List<T> parseArray(String jsonString, Class<T> clazz) {
	        return JSONObject.parseArray(jsonString, clazz);
	    }

		@Override
		public Map<String, Object> parseMap(String jsonString) {
			return JSONObject.parseObject(jsonString);
		}

}
