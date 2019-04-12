package com.isoftstone.common.mongobackup.service.common;

public interface FileService {
	
	void insertFile(String filePath,String contentType,String fileName);	
	int getFileInputStream(String fileName); 
	
}
