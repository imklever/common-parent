package com.isoftstone.common.api.controller.mongo;

import com.isoftstone.common.mongo.FileServiceClient;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(ApiMapperUrlConstants.FILE)
public class MongoFileEndpoint {
	@Autowired
    FileServiceClient fileServiceClient;
	
    //文件下载相关代码
    @RequestMapping("/getMongoFile")
    public int downloadFile(@RequestParam(value = "fileName", required = true,defaultValue="")String fileName){
    	return fileServiceClient.getFileInputStream(fileName);
    }
 }
