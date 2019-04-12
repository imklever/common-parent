package com.isoftstone.common.mongobackup;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.common.MongoSqlDto;
import com.isoftstone.common.mongobackup.domain.mysql.DeviceDto;
import com.isoftstone.common.mongobackup.service.common.jd.DeviceDtoService;
import com.isoftstone.common.util.JsonService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.result.DeleteResult;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceBackupMongoApplicationTest {
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	JsonService jsonService;
	@Autowired
	DeviceDtoService deviceDtoService;
	@Test	
	public void testInstall() throws Exception{
		MongoSqlDto mogoSqlDto=new MongoSqlDto();
		mogoSqlDto.setQuery("{'store_id':'${store_id}','device':'${device}'}");
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		
		paramsMap.put("store_id", "store_id_10001");
		paramsMap.put("device", "device_id");
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_0);  
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("option",mogoSqlDto.getQuery());
		configuration.setTemplateLoader(stringLoader);
		configuration.setDefaultEncoding("UTF-8");
		Template template;
		template = configuration.getTemplate("option","utf-8");
		Writer writer  =  new StringWriter();
		template.process(paramsMap, writer);
		String t=writer.toString().replaceAll("\n", " ").replaceAll("\\s"," ").replaceAll("\r"," ");
		mogoSqlDto.setQuery(t);
		
		mogoSqlDto.setTable("store_device_collection");
		
		
		System.out.println(jsonService.toJson(mogoSqlDto));
		
		
	    Query query=new BasicQuery(mogoSqlDto.getQuery());
	    DeleteResult d=mongoTemplate.remove(query, mogoSqlDto.getTable());
	    System.out.println(jsonService.toJson(d));
	    
		
		/**
		 * 第一种方式:全部删除，之后再插入
		 */
		//List<DeviceDto> list=deviceDtoService.selectByTypeId(null);
		
	    /*DeleteResult deleteResult= mongoTemplate.remove(query, mogoSqlDto.getTable());
	    System.out.println(jsonService.toJson(deleteResult));*/
		//List<Map> json= mongoTemplate.find(query, Map.class,"store_device_collection");
		//if(list.size()!=json.size()) {
			/*mongoTemplate.remove(query, "store_device_collection");//先删除再添加
			for (DeviceDto deviceDto : list) {
				if(!deviceDto.getTypeid().equals("3")) {
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("name", deviceDto.getStoreId());
					map.put("device", deviceDto.getMac());
					mongoTemplate.insert(map, "store_device_collection");
				}
			}*/
		//}
		
		/*System.out.println(list.size());*/
		//mongoTemplate.createCollection("test");
		/*Map<String,Object> map=new HashMap<String, Object>();
		map.put("name", "张三");
		map.put("password", "密码");
		map.put("age", 18);
		mongoTemplate.insert(map, "test");*/
		//JSONObject obj=JSONObject.parseObject("{table:'test',query:{'$or':[{'password':'密码'},{age:{'$gt':16}}]}}");
		//System.out.println(obj);
	}
	public void testqud() throws Exception {
		MongoSqlDto mogoSqlDto=new MongoSqlDto();
		mogoSqlDto.setTable("store_device_collection");
		mogoSqlDto.setInsert("{'store_id':'${store_id}','device':'${device}'}");
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put("store_id", "store_id_10001");
		paramsMap.put("device", "device_id");
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_0);  
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("option",mogoSqlDto.getInsert());
		configuration.setTemplateLoader(stringLoader);
		configuration.setDefaultEncoding("UTF-8");
		Template template;
		template = configuration.getTemplate("option","utf-8");
		Writer writer  =  new StringWriter();
		template.process(paramsMap, writer);
		String t=writer.toString().replaceAll("\n", " ").replaceAll("\\s"," ").replaceAll("\r"," ");
		System.out.println(t);
		mogoSqlDto.setInsert(t);
		System.out.println(jsonService.toJson(mogoSqlDto));
		System.out.println(jsonService.parseObject(t));
		mongoTemplate.insert(jsonService.parseObject(t), mogoSqlDto.getTable());
	}
	//查询
	public void testRm() throws Exception {
		MongoSqlDto mogoSqlDto=new MongoSqlDto();
		mogoSqlDto.setQuery("{'store_id':'${store_id}','device':'${device}'}");
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		
		paramsMap.put("store_id", "store_id_100021");
		paramsMap.put("device", "device_id");
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_0);  
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("option",mogoSqlDto.getQuery());
		configuration.setTemplateLoader(stringLoader);
		configuration.setDefaultEncoding("UTF-8");
		Template template;
		template = configuration.getTemplate("option","utf-8");
		Writer writer  =  new StringWriter();
		template.process(paramsMap, writer);
		String t=writer.toString().replaceAll("\n", " ").replaceAll("\\s"," ").replaceAll("\r"," ");
		mogoSqlDto.setQuery(t);
		
		mogoSqlDto.setTable("store_device_collection");
		
		
		System.out.println(jsonService.toJson(mogoSqlDto));
		
		
	    Query query=new BasicQuery(mogoSqlDto.getQuery());
	    
	    List<Map> json= mongoTemplate.find(query, Map.class,mogoSqlDto.getTable());
		 System.out.println(json.size());
		 for (Map jsonObject : json) {
			System.out.println(jsonService.toJson(jsonObject));
		}
	}
	//@Test	
	public void testQuery() {
		// Query query=new BasicQuery("{'$or':[{'password':'qwe'},{'age':{'$gt':16}}]},{'name':1,'_id':0,'password':0}") {}   ;
		 Query query=new BasicQuery("{}");
		 //query.with(new Sort(Direction.ASC, "face_id"));
		 /* List<Sort.Order>orders=new ArrayList<Sort.Order>();
	       orders.add(new Sort.Order(Direction.ASC, "a"));
	       orders.add(new Sort.Order(Direction.DESC, "b"));
	       query.with(new Sort(orders));*/
	       //query.limit(1);
		   //query.skip(1);
		// query.with(PageRequest.of(1, 2));
		 List<Map> json= mongoTemplate.find(query, Map.class,"store_device_collection");
		 System.out.println(json.size());
		 for (Map jsonObject : json) {
			System.out.println(jsonService.toJson(jsonObject));
		}
	}
	public void del() {
		 Query query=new BasicQuery("{'$or':[{'password':'密码'},{age:{$gt:16}}]},{'name':1,'_id':0,'password':0}") {}   ;
		 mongoTemplate.remove(query, "test");
	}
	
	public void update() {
		Query query=new BasicQuery("{'$or':[{'password':'密码'},{age:{$gt:16}}]},{'name':1,'_id':0,'password':0}") {}   ;
		Update update=Update.update("", "");
		mongoTemplate.upsert(query, update,  "test");
	}
	
	
}
