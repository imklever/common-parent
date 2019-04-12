package com.isoftstone.common.api.service.cache;

import java.util.concurrent.ConcurrentHashMap;

public interface CacheService {
	/**
	 * 根据类型保存对应的值
	 * @param type 类型：user,role,menu,permission
	 * @param key  格式：user_tokenid 形式 如：user_1244
	 * @param obj  数据：可以是对象 也可以是 map
	 */
	void put(String type,String key,Object obj);
	
	/**
	 * 根据类型，key来获取值 
	 * @param type  类型：user,role,menu,permission
	 * @param key   格式：user_tokenid 形式 如：user_1244
	 * @return
	 */
	Object get(String type,String key);
	
	/**
	 *  根据类型 token的值清空用户缓存
	 * @param type 类型：user
	 * @param token  格式：user_tokenid 形式 如：user_1244
	 * @param userId  用户ID
	 */
	void delUserToken(String type,String token,String userId);
	
	/**
	 * 根据类型获取对应类型的值{弃用}
	 * @param type  类型：user,role,menu,permission
	 * @return
	 */
	ConcurrentHashMap<String, Object> getByType(String type);
	
	/**
	 *  检查token是否存在
	 * @param overDueTime 过期时间
	 * @param token   
	 * @return
	 */
	boolean checkToken(String overDueTime,String token);
	
	/**
	 * 重置过期时间
	 * @param token  
	 * @param expiredTime
	 */
	void resetExpiredTime(String token,long expiredTime);
	
	/**
	 * 判断是否存在token
	 * @param type
	 * @param token
	 * @return
	 */
	boolean containsToken(String type,String token);
}
