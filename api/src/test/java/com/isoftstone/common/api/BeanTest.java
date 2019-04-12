package com.isoftstone.common.api;


import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import com.isoftstone.common.common.CommTreeDto;

public class BeanTest {
	public static void main(String[] args) {
		/*CommTreeDto commTreeDto=new CommTreeDto();
		BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(CommTreeDto.class);
        Object boj=generator.create();
        BeanMap beanMap=BeanMap.create(boj);
        beanMap.put("idp", "id");
        System.out.println( beanMap.getOrDefault("idp", "0"));
        beanMap=BeanMap.create(boj);
        beanMap.put("idp", "id2");
        System.out.println( beanMap.getOrDefault("idp", "0"));*/
		/*String aa="a   ' \"	t";
		 //aa="a  t";
		System.out.println(aa.replaceAll("[\\s'\"]+", "_"));*/
		String aa= "user_12";
		System.out.println(aa.replace("user_", ""));
		
		/*Double d=310.0;
		System.out.println(d.intValue());*/
		
	}
}
