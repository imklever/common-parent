package com.isoftstone.common.api.support;

import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.common.sys.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheManageRunner {

	@Value("${cache.type:javaCache}")
	String cacheType;
	@Autowired
	LocalCacheFactory localCacheFactory;
  
	@Scheduled(fixedDelay=900000)
	public void run() throws Exception {
		CacheService cacheService = localCacheFactory
				.getCacheService(cacheType);
	 
			// 清空用户缓存
			clearUserCache(cacheService);
			// 清空验证码缓存
			clearValidCode(); 
	}

	private void clearUserCache(CacheService cacheService) {
		if ("javaCache".equals(cacheType)) {
			ConcurrentHashMap<String, Object> currentMap = cacheService
					.getByType("user");
			if (currentMap != null) {
				// 此方法为缓存是java内存中使用
				for (String key : currentMap.keySet()) {
					boolean flag = true;
					String id = "";
					if (currentMap.get(key) instanceof SysUserDto) {
						SysUserDto sysUserDto = (SysUserDto) currentMap
								.get(key);
						String overDueTime = sysUserDto.getOverduetime();
						String token = sysUserDto.getToken();
						flag = cacheService.checkToken(overDueTime, token);
						if (!flag) {
							id = sysUserDto.getId();
						}
					}
					if ((!StringUtils.isEmpty(id))
							&& currentMap.containsKey("user_" + id)) {
						currentMap.remove("user_" + id);
					}
				}
			}
		}
	}

	private void clearValidCode() {
		ConcurrentHashMap<String, Object> codeMap = BaseConfig.VALIDATE_CODE_HASH_MAP;
		for (String key : codeMap.keySet()) {
			HashMap<String, Object> map = (HashMap<String, Object>) codeMap
					.get(key);
			String overDueTime = (String) map.get("expiredTime");
			long timeDiff = new Date().getTime() - Long.parseLong(overDueTime);
			if (timeDiff > 0) {
				BaseConfig.VALIDATE_CODE_HASH_MAP.remove(key);
			}
		}

	}

}
