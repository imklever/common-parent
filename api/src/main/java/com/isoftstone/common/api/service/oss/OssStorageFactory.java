package com.isoftstone.common.api.service.oss;

import com.isoftstone.common.api.service.oss.Impl.JDOssStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OssStorageFactory {
	@Autowired
	JDOssStorageServiceImpl jDOssStorageServiceImpl;
	
    private Map<String, OssStorageService> getOssStorage() {
		Map<String, OssStorageService> ossMap=new HashMap<String, OssStorageService>();
		ossMap.put("JD", jDOssStorageServiceImpl);
		return ossMap;
	}
    
    public OssStorageService getInstance(String type){    	 
		return getOssStorage().get(type);
    }
}
