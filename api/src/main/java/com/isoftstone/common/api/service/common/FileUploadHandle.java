package com.isoftstone.common.api.service.common;

import java.util.List;
import java.util.Map;

import com.isoftstone.common.common.FileAttrDto;

public interface FileUploadHandle {
	
	Object file(FileAttrDto fileAttrDto,Map<String,Object>ossMapList,String type,String upBusinessCode);

	void fileUp(Map<String, Object> fileMap, List<Map<String, Object>> ossMapList, String upbusinessCode);
}
