package com.isoftstone.common.mongo.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongo.FileServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class HystrixFileServiceClient implements FileServiceClient{
	
	@Autowired
	FileServiceClient fileServiceClient;
 
	@Override
	@HystrixCommand(fallbackMethod = "getFileInputStreamFallBackCall")
	public int getFileInputStream(String fileName) {		
		return fileServiceClient.getFileInputStream(fileName);
	}
	
	public int getFileInputStreamFallBackCall(String fileName) {
		// TODO Auto-generated method stub
		return 0;
	}


}
