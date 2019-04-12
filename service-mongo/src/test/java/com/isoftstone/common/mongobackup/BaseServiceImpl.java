package com.isoftstone.common.mongobackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.gridfs.model.GridFSFile;


@Service
public class BaseServiceImpl{
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	GridFsTemplate gridFsTemplate;
	
	public List<Account> find() {		
		return mongoTemplate.find(new Query(), Account.class);		
	}
	
	public void insert(Account account){
		mongoTemplate.insert(account);
	}
	
	public long update(Account account){
		Query query =new Query();
		//query.addCriteria(Criteria.where("name").is("zhangsan"));
		Update update =new Update();
		update.set("name", 111);
		return mongoTemplate.updateMulti(query, update, "account").getModifiedCount();
	}
	
	public long delete(String name){
		Query query =new Query();
		query.addCriteria(Criteria.where("name").is(name));
		return mongoTemplate.remove(query, Account.class).getDeletedCount();
	}
	
	
	public void saveImage(String imagePath) throws IOException{
		File imageFile = new File(imagePath);
		InputStream inputStream = new FileInputStream(imageFile);
		String contentTypeString="";
		gridFsTemplate.store(inputStream, "test1",contentTypeString);
		
	}
	
	public void getImage(String imagePath) throws IOException{
		
		 Query query = Query.query(Criteria.where("filename").is("test"));
	     
	     GridFSFile gfsFile = gridFsTemplate.findOne(query);
	      
	     String fileNameString = gfsFile.getFilename();
	     InputStream inputStream = gridFsTemplate.getResource(fileNameString).getInputStream();
		 
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
	     
	     //System.out.println(inputStream.read());
	}
	
}
