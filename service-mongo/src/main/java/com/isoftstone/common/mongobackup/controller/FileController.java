package com.isoftstone.common.mongobackup.controller;

import org.common.constant.MongoServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.mongo.FileServiceDefinition;
import com.isoftstone.common.mongobackup.service.common.FileService;

@RefreshScope
@RestController
@RequestMapping(name = MongoServiceBackConstants.BACK_SERVICE,
 	path = MongoServiceBackConstants.PATH_SYS_FILE)
public class FileController  implements FileServiceDefinition{

	@Autowired
	FileService FileService;
	@Override
	public int getFileInputStream(@RequestParam(value = "fileName", required = true)String fileName) {	
		return FileService.getFileInputStream(fileName);
	}

}
