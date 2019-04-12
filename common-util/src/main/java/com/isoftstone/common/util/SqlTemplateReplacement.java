package com.isoftstone.common.util;

import java.util.Map;


public interface SqlTemplateReplacement {
	/**
	 * sql模板替换
	 * @param paramsMap
	 * @param sqlTemplate
	 * @return
	 */
	String templateReplacement(Map<String, Object>paramsMap,String sqlTemplate) throws Exception;
	/**
	 * 创建map,对模板中的数据进行替换，
	 * @param paramsMap
	 * @param inputJson
	 * @param params
	 * @return
	 */
	Map<String, Object>  createReplaceMap(Map<String, Object> paramsMap, String inputJson, String params);
	/**
	 * 
	 * @param versionNo 版本号
	 * @param paramsMap 所有参数
	 * @param inputJson 输入参数描述
	 * @param params    前台输入参数
	 * @param isTest    是否测试
	 * @return
	 */
	Map<String, Object>  createReplaceMap(int versionNo, Map<String, Object> paramsMap, String inputJson, String params,boolean isTest);
	/**
	 * 创建map,对模板中的数据进行替换，
	 * @param paramsMap
	 * @param inputJson
	 * @param params
	 * @return
	 */
	Map<String, Object>  createReplaceTestMap(Map<String, Object> paramsMap, String inputJson);
	/**
	 * 
	 * @param inputData
	 * @return
	 */
	String createReplaceTestStr(String inputData);
}
