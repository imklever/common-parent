package com.isoftstone.common.mongobackup.service.common.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.isoftstone.common.mongobackup.service.common.FileService;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	GridFsTemplate gridFsTemplate;

	@Override
	public void insertFile(String filePath, String contentType, String fileName) {
		File file = new File(filePath);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			gridFsTemplate.store(inputStream, fileName, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getFileInputStream(String fileName) {
		Query query = Query.query(Criteria.where("filename").is(fileName));

		GridFSFile gfsFile = gridFsTemplate.findOne(query);

		String fileNameString = gfsFile.getFilename();
		InputStream inputStream = null;
		try {
			inputStream = gridFsTemplate.getResource(fileNameString)
					.getInputStream();
			byte[] bs = new byte[1024 * 2];
			// 读取到的数据长度
			int len;
			// 输出的文件流保存图片至本地
			OutputStream os = new FileOutputStream("E:\\aa.png");
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		return 1;
	}

}
