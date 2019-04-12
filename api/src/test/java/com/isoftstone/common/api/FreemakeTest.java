package com.isoftstone.common.api;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.util.CommUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class FreemakeTest {
	public static void main(String[] args) throws TemplateNotFoundException, 
		MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		CommUtil.getMD5("15303598140"+"9").toLowerCase();
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		String tets1="[{\"welcome\":[],\"welcome_details\":[],\"resource\":[\"8303d55e9ba743cf91adc73868ff0078\"],\"resource_details\":[\"http://s3.cn-north-1.jcloudcs.com/isoftstone/20180909/20180909162258_Jellyfish.jpg\"],\"gender\":-1,\"is_default\":0,\"max_age\":0,\"min_age\":0,\"store_id\":\"38e3f74fb27f11e8b7d5fa163ec0ad5d\",\"device_id\":\"3b057634b3d511e8b7d5fa163ec0ad5d\"}]";
		String tets="[{\"welcome\":[\"77ecbd59388948f092c8f31757f9bf04\"],\"welcome_details\":[\"http://s3.cn-north-1.jcloudcs.com/isoftstone/20180909/20180909163930_Sleep Away.mp3\"],\"resource\":[\"e4c4c1e9e63448dda249ef0435a03c29\"],\"resource_details\":[\"http://s3.cn-north-1.jcloudcs.com/isoftstone/20180909/20180909163933_Chrysanthemum.jpg\"],\"gender\":-1,\"is_default\":0,\"max_age\":0,\"min_age\":0,\"store_id\":\"38e3f74fb27f11e8b7d5fa163ec0ad5d\",\"device_id\":\"3b057634b3d511e8b7d5fa163ec0ad5d\"}]";
		String sqlTemplate="INSERT INTO jd_config_rule(id,device_id,"
				+ "store_id,max_age,min_age"
				+ ",gender,welcome,welcome_details,resource,resource_details)"
				+ " values \r\n"  
				+ "<#list fileList as data> " + 
				"(UUID(),"+
				"'${data['device_id']}','${data['store_id']}','${data['max_age']}','${data['min_age']}'"+
				 ",'${data['gender']}',"
				 + "'<#list data['welcome'] as welcomedata><#if (welcomedata_index == 0)>[</#if>\"${welcomedata}\"<#if (welcomedata_index+1 != data['welcome']?size)>,</#if><#if (welcomedata_index+1 == data['resource']?size)>]</#if></#list>'"
				 + ",'<#list data['welcome_details'] as welcomedata><#if (welcomedata_index == 0)>[</#if>\"${welcomedata}\"<#if (welcomedata_index+1 != data['welcome_details']?size)>,</#if><#if (welcomedata_index+1 == data['welcome_details']?size)>]</#if></#list>'"
				 + ",'<#list data['resource'] as resourcedata><#if (resourcedata_index == 0)>[</#if>\"${resourcedata}\"<#if (resourcedata_index+1 != data['resource']?size)>,</#if><#if (resourcedata_index+1 == data['resource']?size)>]</#if></#list>'"
				 + ",'<#list data['resource_details'] as resourcedata><#if (resourcedata_index == 0)>[</#if>\"${resourcedata}\"<#if (resourcedata_index+1 != data['resource_details']?size)>,</#if><#if (resourcedata_index+1 == data['resource_details']?size)>]</#if></#list>'"
				+" <#if (data_index+1 != fileList?size)>,</#if>\r\n" + 
				"</#list>)";
		JSONArray k=JSONArray.parseArray(tets);
		System.out.println(k);
		paramsMap.put("fileList",k);
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_0);  
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("option",sqlTemplate);
		configuration.setTemplateLoader(stringLoader);
		configuration.setDefaultEncoding("UTF-8");
		Template template;
		template = configuration.getTemplate("option","utf-8");
		Writer writer  =  new StringWriter();
		template.process(paramsMap, writer);
		String t=writer.toString().replaceAll("\n", " ").replaceAll("\\s"," ").replaceAll("\r"," ");
		System.out.println(t);
}

}
