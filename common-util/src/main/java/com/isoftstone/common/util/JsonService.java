package com.isoftstone.common.util;

import java.util.List;
import java.util.Map;

public interface JsonService {

	String toJson(Object object);

    Object parseObject(String jsonString);
    
    Map<String, Object> parseMap(String jsonString);

    <T> T parseObject(String jsonString, Class<T> clazz);

    <T> List<T> parseArray(String jsonString, Class<T> clazz);
}
