package com.isoftstone.common.api.util;

import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class RequestParamsUtils {

	@Value("${cache.type:javaCache}")
	String cacheType;
	
	@Autowired
	JsonService jsonService;
	@Autowired
	LocalCacheFactory localCacheFactory;
	
	static	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	
	public  String rebuildParam(String params) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		params = rebuildParam(request, params);
		return params;
	}

	@SuppressWarnings("unchecked")
	public  String rebuildParam(HttpServletRequest request, String params) {
		if (request != null) {
			String token = request.getHeader("token");
			if (!StringUtils.isEmpty(token)) {
			/*	ConcurrentHashMap<String, Object> userMap = BaseConfig.USER_HASH_MAP;
				ConcurrentHashMap<String, Object> roleMap =BaseConfig.USER_ROLE_HASH_MAP;*/
			
				java.util.Map<String, Object> paramMap = jsonService.parseMap(params);
				java.util.Map<String, Object> newUserMap = new HashMap<String, Object>();

				CacheService cacheService = localCacheFactory.getCacheService(cacheType);
				cacheService.get("user", token);
				
				newUserMap.put("user", cacheService.get("user", token));
				newUserMap.put("role", cacheService.get("role", token));
				newUserMap.put("sys_date", sdf.format(new Date()));
				paramMap.put("system", newUserMap);
				return jsonService.toJson(paramMap);
			}
		}
		return params;
	}

}
