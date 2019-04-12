package com.isoftstone.common.mongobackup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.jd.FaceIdCollectionDto;
import com.isoftstone.common.jd.FaceLogCollectionDto;
import com.isoftstone.common.jd.SyncScheduleDto;
import com.isoftstone.common.mongobackup.domain.FaceIdCollection;
import com.isoftstone.common.mongobackup.domain.LogCollection;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceIdCollectionService;
import com.isoftstone.common.mongobackup.service.common.jd.FaceLogCollectionDtoService;
import com.isoftstone.common.mongobackup.service.common.jd.LogCollectionService;
import com.isoftstone.common.mongobackup.service.common.jd.SyncScheduleDtoService;
import com.netflix.discovery.shared.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceBackupMongoApplicationTests {

	@Autowired
	BaseServiceImpl baseServiceImpl;
	@Autowired
	FaceIdCollectionService faceIdCollectionService;
	@Autowired
	LogCollectionService logCollectionService;
	@Autowired
	JsonService jsonService;
	@Autowired
	FaceIdCollectionDtoService faceIdCollectionDtoService;
	@Autowired
	FaceLogCollectionDtoService faceLogCollectionDtoService;
	
	@Autowired 
	SyncScheduleDtoService syncScheduleDtoService;
	
	@Test
	public void contextLoads() {
		
	//List<FaceIdCollection> list =faceIdCollectionService.getByTime("1533559826555", "1533659826560");
	//List<LogCollection> logList=logCollectionService.getByTime("1533559826555", "1533659826560");
	
	/*List<FaceIdCollectionDto> faceIdCollectionDtos = new ArrayList<FaceIdCollectionDto>();
	for (FaceIdCollection faceIdCollection : list) {
		FaceIdCollectionDto faceIdCollectionDto =new FaceIdCollectionDto();
		faceIdCollectionDto.setId(faceIdCollection.getId());
		faceIdCollectionDto.setAge(faceIdCollection.getAge());
		faceIdCollectionDto.setAppearedNum(faceIdCollection.getAppeared_num());
		faceIdCollectionDto.setCaptureTime(faceIdCollection.getCapture_time());
		faceIdCollectionDto.setDevice(faceIdCollection.getDevice());
		faceIdCollectionDto.setFaceId(faceIdCollection.getFace_id());
		faceIdCollectionDto.setFeature(faceIdCollection.getFeature());
		faceIdCollectionDto.setGender(faceIdCollection.getGender());
		faceIdCollectionDto.setImage(faceIdCollectionDto.getImage());
		
		faceIdCollectionDtos.add(faceIdCollectionDto);
	}*/
	
	/*List<FaceLogCollectionDto> faceLogCollectionDtos = new ArrayList<FaceLogCollectionDto>();
	for (LogCollection logCollection : logList) {
		FaceLogCollectionDto faceLogCollectionDto =new FaceLogCollectionDto();
		faceLogCollectionDto.setId(logCollection.getId());
		faceLogCollectionDto.setFaceId(logCollection.getFace_id());
		faceLogCollectionDto.setDevice(logCollection.getDevice());
		faceLogCollectionDto.setAppearedTime(logCollection.getAppeared_time());
		faceLogCollectionDto.setImage(logCollection.getImage());
		
		faceLogCollectionDtos.add(faceLogCollectionDto);
	}*/
	
	//faceIdCollectionDtoService.insertBatch(faceIdCollectionDtos);
	//faceLogCollectionDtoService.insertBatch(faceLogCollectionDtos);
	
	/*System.out.println("Face Id collection:"+jsonService.toJson(list));
	System.out.println("Log collection:"+jsonService.toJson(logList));*/
	
	//新增同步时间
/*	String id = UUID.randomUUID().toString().replaceAll("-", "") + "";
	String syncTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
	SyncScheduleDto syncScheduleDto =new SyncScheduleDto();
	syncScheduleDto.setId(id);
	syncScheduleDto.setSyncTime(syncTime);
	syncScheduleDtoService.insert(syncScheduleDto);*/
 	
	/*	//增加
	    Account account=new Account();
		account.setName("zhangsan1");
		account.setPassword("zhangsan1");
		baseServiceImpl.insert(account);
		
		//修改
		Account account2 = new Account();
		account.setName("zhangsan2");
		account.setPassword("zhangsan2");
		long updateCount = baseServiceImpl.update(account2);
		
		System.out.println("update count: "+updateCount);
		
		//删除
		long deleteCount = baseServiceImpl.delete("zhangsan1");
		
		System.out.println("delete count:"+String.valueOf(deleteCount));
		
		//查询
		List<Account> list = baseServiceImpl.find();
		for (Account account1 : list) {
			System.out.println("account:::: name :"+account1.getName()+",password:"+account1.getPassword());
		}*/
		
		//保存图片
	/*	try {
			baseServiceImpl.saveImage("E:\\mogondb_test.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//读取图片
		try {
			baseServiceImpl.getImage("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}