package com.isoftstone.common.api.support;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存配置类
 * 
 * @author xwwangal
 *
 */
public class BaseConfig {
	public final static ConcurrentHashMap<String, Object> USER_HASH_MAP = new ConcurrentHashMap<String, Object>();
	public final static ConcurrentHashMap<String, Object> USER_ROLE_HASH_MAP = new ConcurrentHashMap<String, Object>();
	public final static ConcurrentHashMap<String, Object> USER_MENU_HASH_MAP = new ConcurrentHashMap<String, Object>();
	public final static ConcurrentHashMap<String, Object> USER_PERMISSION_HASH_MAP = new ConcurrentHashMap<String, Object>();
	public final static ConcurrentHashMap<String, Object> VALIDATE_CODE_HASH_MAP = new ConcurrentHashMap<String, Object>();	
	public final static ConcurrentHashMap<String, Object> WX_APP_CONINFG_HASH_MAP = new ConcurrentHashMap<String, Object>();
	public final static ConcurrentHashMap<String, Object> WX_APP_ROUTER_HASH_MAP = new ConcurrentHashMap<String, Object>();
	
	public static void setUserHashMap(String key, Object obj) {
		setHashMap(key, obj, USER_HASH_MAP);
	}

	public static void setRoleHashMap(String key, Object obj) {
		setHashMap(key, obj, USER_ROLE_HASH_MAP);
	}

	public static void setMenuHashMap(String key, Object obj) {
		setHashMap(key, obj, USER_MENU_HASH_MAP);
	}

	public static void setPermissionHashMap(String key, Object obj) {
		setHashMap(key, obj, USER_PERMISSION_HASH_MAP);
	}
	public static void setWxConfgHashMap(String key, Object obj) {
		setHashMap(key, obj, WX_APP_CONINFG_HASH_MAP);
	}
	public static void setWxAppRouterConfgHashMap(String key, Object obj) {
        setHashMap(key, obj, WX_APP_ROUTER_HASH_MAP);
    }

	public static void remove(String key){
		if(key!=""){
			if(USER_HASH_MAP.containsKey("user_"+key)){
				USER_HASH_MAP.remove("user_"+key);
			}
			
			if(USER_ROLE_HASH_MAP.containsKey("role_"+key)){
				USER_ROLE_HASH_MAP.remove("role_"+key);
			}
			
			if(USER_MENU_HASH_MAP.containsKey("menu_"+key)){
				USER_MENU_HASH_MAP.remove("menu_"+key);
			}
			
			if(USER_PERMISSION_HASH_MAP.containsKey("permission_"+key)){
				USER_PERMISSION_HASH_MAP.remove("permission_"+key);
			}
		}
	}
	
	private static void setHashMap(String key, Object obj,
			ConcurrentHashMap<String, Object> map) {
		if(map!=null){
			if (map.containsKey(key)) {
				map.replace(key, obj);
			} else {
				map.put(key, obj);
			}
		}
	}

}
