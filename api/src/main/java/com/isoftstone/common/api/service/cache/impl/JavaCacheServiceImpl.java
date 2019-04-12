package com.isoftstone.common.api.service.cache.impl;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.common.constant.CommonConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.support.BaseConfig;
import com.isoftstone.common.common.sys.SysUserDto;

@Service
public class JavaCacheServiceImpl implements CacheService {

	@Override
	public void put(String type, String key, Object obj) {
		String mapKey = type+"_"+key;
		
		if(obj!=null)
		{	
			switch (type) {
				case CommonConstants.CACHE_TYPE_KEY_USER:
					BaseConfig.setUserHashMap(mapKey, obj);
					break;
				case CommonConstants.CACHE_TYPE_KEY_ROLE:
					BaseConfig.setRoleHashMap(mapKey, obj);
					break;
				case CommonConstants.CACHE_TYPE_KEY_MENU:
					BaseConfig.setMenuHashMap(mapKey, obj);
					break;
				case  CommonConstants.CACHE_TYPE_KEY_PERMISSION:
					BaseConfig.setPermissionHashMap(mapKey, obj);
					break;
				case  CommonConstants.CACHE_TYPE_KEY_WXAPP:
					BaseConfig.setWxConfgHashMap(mapKey, obj);
					break;
				case  CommonConstants.CACHE_TYPE_KEY_WXAPP_ROUTER:
                    BaseConfig.setWxAppRouterConfgHashMap(mapKey, obj);
                    break;
				default:
					break;
			}
		}
	}

	@Override
	public Object get(String type, String key) {
		String mapKey = type+"_"+key;
		Object obj=null;
		switch (type) {
			case CommonConstants.CACHE_TYPE_KEY_USER:
				obj = BaseConfig.USER_HASH_MAP.get(mapKey);
				break;
			case CommonConstants.CACHE_TYPE_KEY_ROLE:
				obj = BaseConfig.USER_ROLE_HASH_MAP.get(mapKey);
				break;
			case CommonConstants.CACHE_TYPE_KEY_MENU:
				obj = BaseConfig.USER_MENU_HASH_MAP.get(mapKey);
				break;
			case CommonConstants.CACHE_TYPE_KEY_PERMISSION:
				obj = BaseConfig.USER_PERMISSION_HASH_MAP.get(mapKey);
				break;
			case CommonConstants.CACHE_TYPE_KEY_WXAPP://微信
				obj = BaseConfig.WX_APP_CONINFG_HASH_MAP.get(mapKey);
				break;
			case CommonConstants.CACHE_TYPE_KEY_WXAPP_ROUTER://微信
                obj = BaseConfig.WX_APP_ROUTER_HASH_MAP.get(mapKey);
                break;
			default:
				break;
		}
		return obj;
	}

	@Override
	public void delUserToken(String type,String token,String userId) {
		Object oldUserToken= get(type, userId);		
		if (!StringUtils.isEmpty(oldUserToken)) {
			if (!(type+"_" + token).equals(oldUserToken.toString())) {
				 BaseConfig.remove(oldUserToken.toString().replace(type+"_", ""));
			}
		}	 
	  
	}

	@Override
	public ConcurrentHashMap<String, Object> getByType(String type) {
		ConcurrentHashMap<String, Object> obj=null;
		switch (type) {
			case CommonConstants.CACHE_TYPE_KEY_USER:
				obj = BaseConfig.USER_HASH_MAP;
				break;
			case CommonConstants.CACHE_TYPE_KEY_ROLE:
				obj = BaseConfig.USER_ROLE_HASH_MAP;
				break;
			case CommonConstants.CACHE_TYPE_KEY_MENU:
				obj = BaseConfig.USER_MENU_HASH_MAP;
				break;
			case CommonConstants.CACHE_TYPE_KEY_PERMISSION:
				obj = BaseConfig.USER_PERMISSION_HASH_MAP;
				break;
			default:
				break;
		}
		return obj;
	}

	@Override
	public boolean checkToken(String overDueTime, String token) {
		long timeDiff = new Date().getTime()-Long.parseLong(overDueTime);
	    if(timeDiff>0)
	    {
	    	 BaseConfig.remove(token);
	    	return false;
	    }
	    else {
	    	return true;
	    }
	}

	@Override
	public void resetExpiredTime(String token,long expiredTime) {
		Object obj = get(CommonConstants.CACHE_TYPE_KEY_USER, token);
		
		if(obj instanceof SysUserDto){			
			 SysUserDto sysUserDto = (SysUserDto)get(CommonConstants.CACHE_TYPE_KEY_USER, token);
		     if(sysUserDto!=null)
		  	 {
		  		Long duetime = new Date().getTime()+expiredTime*60*1000;
		  		String overduetime=String.valueOf(duetime);
		  		sysUserDto.setOverduetime(overduetime);		
		  		
		  		put(CommonConstants.CACHE_TYPE_KEY_USER, token, sysUserDto);
		  	}
		}
		
	}

	@Override
	public boolean containsToken(String type, String token) {
		boolean flag =false;
		String key = type+"_"+token;
		
		switch (type) {
		case CommonConstants.CACHE_TYPE_KEY_USER:
			flag=BaseConfig.USER_HASH_MAP.containsKey(key);
			break;
		case CommonConstants.CACHE_TYPE_KEY_ROLE:
			flag=BaseConfig.USER_ROLE_HASH_MAP.containsKey(key);
			break;
		case CommonConstants.CACHE_TYPE_KEY_MENU:
			flag=BaseConfig.USER_MENU_HASH_MAP.containsKey(key);
			break;
		case  CommonConstants.CACHE_TYPE_KEY_PERMISSION:
			flag=BaseConfig.USER_PERMISSION_HASH_MAP.containsKey(key);
			break;
		default:
			break;
		}
		return flag;
	}

}
