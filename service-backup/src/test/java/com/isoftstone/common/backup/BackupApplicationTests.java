package com.isoftstone.common.backup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.isoftstone.common.ReflexClassDto;
import com.isoftstone.common.backup.service.common.sys.SysJdbcDtoService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SpringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackupApplicationTests {
	//@Autowired
	//SysJdbcDtoService sysJdbcDtoService;
	/*@Autowired
	CommonSqlService commonSqlService;*/
	@Autowired
	JsonService jsonService;
	@Test
	public void contextLoads() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ReflexClassDto  reflexClassDto=new ReflexClassDto();
		reflexClassDto.setClassObj("com.isoftstone.common.backup.service.common.ServiceTest");
		reflexClassDto.setMethod("test");
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("buscode", "385866");
		reflexClassDto.setParams(jsonService.toJson(map));
		
		/*ReflexClassDto  reflexClassDto=jsonService.parseObject("{\"method\":\"test\",\"classObj\":\"com.isoftstone.common.backup.service.common.ServiceTest\",\"params\":\"{'buscode':'385866'}\"}", ReflexClassDto.class);
		System.out.println(jsonService.toJson(reflexClassDto));
		String classType=reflexClassDto.getClassObj();
		Class stuClass =Class.forName(classType);
		Object obj =SpringUtil.getBean(stuClass);
		//CommonSqlService commonSqlService= (CommonSqlService) SpringUtil.getBean(Class.forName(classType));
		//SELECT * FROM jd_recordt WHERE userID=2 AND id='3'
		Method m =stuClass.getDeclaredMethod(reflexClassDto.getMethod(), Map.class);
		m.invoke(obj, jsonService.parseMap(reflexClassDto.getParams()));*/
		/*List<Map<String, Object>> list=(List<Map<String, Object>>) m.invoke(obj, "SELECT DATE_FORMAT(timet,'%Y-%m-%d %H:%i:%s')timet FROM jd_usert", "{}", null);
		//commonSqlService.getSqlQuery("SELECT DATE_FORMAT(timet,'%Y-%m-%d %H:%i:%s')timet FROM jd_usert", "{}", null);
		for (Map<String, Object> map : list) {
			
			System.out.println(map.get("timet"));
		}*/
		//System.out.println(sysJdbcDtoService.findAll().size());
	}

}
