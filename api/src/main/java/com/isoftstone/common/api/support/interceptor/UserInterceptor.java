package com.isoftstone.common.api.support.interceptor;


import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.api.service.interceptor.PublicBusinessService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserInterceptor implements HandlerInterceptor{
	
	@Value("${login.expired-time}")
	long expiredTime=0L;
	
	@Value("${cache.type:javaCache}")
	String cacheType;
	@Autowired
	PublicBusinessService publicBusinessService;
	@Autowired
	JsonService jsonService;
	@Autowired
	LocalCacheFactory localCacheFactory;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	String requestUrl = request.getRequestURI();
    	if(requestUrl.contains("sys/user/login"))    	
    		return true;
    	//获取用户的接口 business_code,
    	String  businessCode= request.getParameter("businessCode");
    	//检查接口对应的功能是否被“公共角色”拥有，如果是就跳过登录验证 公共角色的id = 763a7c798f1311e8a5b6fa163ec0ad5d
    	boolean flag= publicBusinessService.checkBusinessCode(businessCode,"763a7c798f1311e8a5b6fa163ec0ad5d");
    	if(flag) {
    		return true;
    	}
    	
    	String token = request.getParameter("token");		
		if(StringUtils.isEmpty(token)){
			token = request.getHeader("token");
		}	
		 
		
    /*	ConcurrentHashMap<String, Object> userMap = BaseConfig.USER_HASH_MAP;
	    SysUserDto sysUserDto = (SysUserDto) userMap.get("user_"+token);*/
		CacheService cacheService =localCacheFactory.getCacheService(cacheType);
		SysUserDto sysUserDto = (SysUserDto) cacheService.get("user", token);
		
	    if(sysUserDto==null) {
	    	APIResult apiResult =new APIResult(-2, "用户未登录！");
    		response.setContentType("application/json;charset=utf-8");  
    		response.getWriter().print(jsonService.toJson(apiResult));
	    	return false;
	    }
	    	    
	    String overDueTime = sysUserDto.getOverduetime();
	    
	    boolean tokenFlag =cacheService.checkToken(overDueTime, token);
	  
	    if(!tokenFlag){
	    	APIResult apiResult =new APIResult(-2, "登录以超时！");
    		response.setContentType("application/json;charset=utf-8");  
    		response.getWriter().print(jsonService.toJson(apiResult));
	    }
	    return tokenFlag;
    }
	
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    
    	String token = request.getParameter("token");		
		if(StringUtils.isEmpty(token)){
			token = request.getHeader("token");
		}	
		CacheService cacheService =localCacheFactory.getCacheService(cacheType);
		cacheService.resetExpiredTime(token, expiredTime);
    }

}
