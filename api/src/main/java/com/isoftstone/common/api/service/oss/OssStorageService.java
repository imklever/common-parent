package com.isoftstone.common.api.service.oss;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface OssStorageService {
	String transferFile(List<Map<String, Object>> mapList, String localFilePath,String remoteRelativePath);
	String transferFile(List<Map<String, Object>> mapList, File localFilePath,String remoteRelativePath);
	String transferFileJust(List<Map<String, Object>> mapList, String localFilePath,String remoteRelativePath);
	String deleteFile(List<Map<String, Object>> mapList,String key_name);
	void copyFile(List<Map<String, Object>> ossMapList, String string, String imgrelativePath);
}
