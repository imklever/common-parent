package com.isoftstone.common.api.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class HttpRequestUtils {
	  public static final String LOCAL_HOST = "127.0.0.1";

	    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
	        Map<String, String> map = new HashMap<String, String>();
	        Enumeration<?> headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            map.put(key, value);
	        }
	        return map;
	    }

	    public static String getHeaderByName(HttpServletRequest request, String headerName) {
	        Map<String, String> headerMap = getHeaderMap(request);

	        if (headerMap != null && headerMap.containsKey(headerName)) {
	            return headerMap.get(headerName);
	        }

	        return null;
	    }

	    public static String getIp(HttpServletRequest request) {
	    	String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    	if(!StringUtils.isEmpty(ip)) {
	    		 return ip;
	    	}
	        ip = request.getRemoteAddr();
	        if (LOCAL_HOST.equals(ip)) {
	            //TODO 可以优化,取了多次header
	            String xRealIp = getHeaderByName(request, "x-real-ip");
	            if (!StringUtils.isEmpty(xRealIp)) {
	                return xRealIp;
	            }

	            String xForwardedFor = getHeaderByName(request, "x-forwarded-for");
	            if (!StringUtils.isEmpty(xForwardedFor)) {
	                return xForwardedFor;
	            }
	        }
	        return ip;
	    }

}
