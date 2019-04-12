package com.isoftstone.common.api.domain.comon;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestMeta {
	   private String remoteAddress;
	    private String requestURI;
	    private Map<String, String[]> parameters;

	    private HttpRequestMeta(HttpServletRequest request) {
	        parameters = request.getParameterMap();
	        requestURI = request.getRequestURI();
	        remoteAddress = request.getRemoteAddr();
	    }

	    public static HttpRequestMeta createByRequest(HttpServletRequest request) {
	        return new HttpRequestMeta(request);
	    }

	    public String getRemoteAddress() {
	        return remoteAddress;
	    }

	    public String getRequestURI() {
	        return requestURI;
	    }

	    public Map<String, String[]> getParameters() {
	        return parameters;
	    }

}
