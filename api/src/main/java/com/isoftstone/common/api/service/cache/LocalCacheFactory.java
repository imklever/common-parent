package com.isoftstone.common.api.service.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.api.service.cache.impl.JavaCacheServiceImpl;
import com.isoftstone.common.api.service.cache.impl.RedisCacheServiceImpl;

@Service
public class LocalCacheFactory{
    @Autowired
    JavaCacheServiceImpl javaCacheServiceImpl;
    @Autowired
    RedisCacheServiceImpl redisCacheServiceImpl;
    
    private Map<String, CacheService> getLocalCache() {
		Map<String, CacheService> cacheServiceMap=new HashMap<String, CacheService>();
		cacheServiceMap.put("javaCache", javaCacheServiceImpl);
		cacheServiceMap.put("redisCache", redisCacheServiceImpl);
		return cacheServiceMap;
	}
    
    public CacheService getCacheService(String cacheType){    	 
		return getLocalCache().get(cacheType);
    }
}
