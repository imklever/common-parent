package com.isoftstone.common.api.service.cache.impl;


import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.support.BaseConfig;
import com.isoftstone.common.util.JsonService;

@SuppressWarnings("unchecked")
@Service
public class RedisCacheServiceImpl implements CacheService{
	
	@Value("${login.expired-time}")
	long expiredTime = 0L;
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
    private JsonService jsonService;
	@Override
	public void put(String type, String key, Object obj) {
       String mapKey = type+"_"+key;
	   	if(obj==null)
			return;
		switch (type) {
		case "user":
		case "role":
		case "menu":
		case "permission":
			redisTemplate.opsForValue().set(mapKey, obj, expiredTime,TimeUnit.MINUTES);
			break;
		default:
			break;
		}
	}

	@Override
	public Object get(String type, String key) {
		String mapKey = type+"_"+key;
		Object obj=null;
		switch (type) {
			case "user":
			case "role":
			case "menu":
			case "permission":
				obj =redisTemplate.opsForValue().get(mapKey);
				break;
			default:
				break;
		}
		return obj;
	}

	@Override
	public ConcurrentHashMap<String, Object> getByType(String type) {
		ConcurrentHashMap<String, Object> obj=null;
	/*	switch (type) {
			case "user":
				obj = BaseConfig.USER_HASH_MAP;
				break;
			case "role":
				obj = BaseConfig.USER_ROLE_HASH_MAP;
				break;
			case "menu":
				obj = BaseConfig.USER_MENU_HASH_MAP;
				break;
			case "permission":
				obj = BaseConfig.USER_PERMISSION_HASH_MAP;
				break;
			default:
				break;
		}*/
		return obj;
	}

	@Override
	public void delUserToken(String type,String token,String userId) {
		Object oldUserToken= get(type, userId);		
		if (!StringUtils.isEmpty(oldUserToken)) {
			if (!(type+"_" + token).equals(oldUserToken.toString())) {
				//redisTemplate.delete(oldUserToken.toString().replace(type+"_", ""));
				
				 String invalidToken=oldUserToken.toString().replace(type+"_", "");
				
				 if(redisTemplate.hasKey("user_"+invalidToken)) {
		    		 redisTemplate.delete("user_"+invalidToken);
		    	 }
		    	 if(redisTemplate.hasKey("role_"+invalidToken)) {
		    		 redisTemplate.delete("role_"+invalidToken);
		    	 }
		    	 if(redisTemplate.hasKey("menu_"+invalidToken)) {
		    		 redisTemplate.delete("menu_"+invalidToken);
		    	 }
		    	 if(redisTemplate.hasKey("permission_"+invalidToken)) {
		    		 redisTemplate.delete("permission_"+invalidToken);
		    	 }
			}
		}	
		
	}

	@Override
	public boolean checkToken(String overDueTime, String token) {	
		
		long timeDiff = new Date().getTime()-Long.parseLong(overDueTime);
	    if(timeDiff>0)
	    {
	    	 if(redisTemplate.hasKey("user_"+token)) {
	    		 redisTemplate.delete("user_"+token);
	    	 }
	    	 if(redisTemplate.hasKey("role_"+token)) {
	    		 redisTemplate.delete("role_"+token);
	    	 }
	    	 if(redisTemplate.hasKey("menu_"+token)) {
	    		 redisTemplate.delete("menu_"+token);
	    	 }
	    	 if(redisTemplate.hasKey("permission_"+token)) {
	    		 redisTemplate.delete("permission_"+token);
	    	 }
	    	return false;
	    }
	    else {
	    	return true;
	    }
	}

	@Override
	public void resetExpiredTime(String token,long expiredTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsToken(String type, String token) {
		return redisTemplate.hasKey(type+"_"+token);
	}


}
