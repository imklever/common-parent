package com.isoftstone.common.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class SqlCreateTest {
	
	public static void main(String[] args) {
		String tableName="cs_step_detail";
		String fields="";
		String keyFields="openid,wx_date,step";
		String whereFields="id,name";
		//createUpdateSql(tableName,fields,keyFields,whereFields);
		
		Date date=new Date();
		System.out.println(System.currentTimeMillis());
		date.setTime(1533312000000L);
		//date.setTime(1533718804413L);
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		System.out.println(sdf.format(date));
		createInstallSql(tableName,fields,keyFields);
	}
	/**
	 * 
	 *  String tableName="sys_user";
	 *	String fields="username,password,token";
	 *	String keyFields="id,name";
	 *	String whereFields="id,name";
	 *	createUpdateSql(tableName,fields,keyFields,whereFields);
	 * 修改语句
	 * @param tableName   表名
	 * @param fields      修改字段可为空字段
	 * @param keyFields   修改字段必须穿字段
	 * @param whereFields where字段
	 */
	private static void createUpdateSql(String tableName, String fields,String keyFields,String whereFields) {
		StringBuffer sb=new StringBuffer("UPDATE ");
		sb.append(tableName).append(" SET (").append("\n");
		String[]fieldArr=fields.split(",");
		/** 非必修改字段 */
		for (int i = 0; i < fieldArr.length; i++) {
			String field = fieldArr[i];
			 sb.append("<#if ").append(field).append("??> \n");
	    	 sb.append(field).append("=? ,");
	    	 sb.append("\n</#if> \n");
		}
		/** 必须修改字段*/
		String[]keyFieldArr=keyFields.split(",");
	    int keyFieldLength=keyFieldArr.length;
	    for (int i = 0; i < keyFieldArr.length; i++) {
			String keyField = keyFieldArr[i];
			 sb.append(keyField).append("=? ");
			if(i!=keyFieldLength-1) {
				sb.append(",");
			}
		}
		sb.append(") WHERE \n ");
		/** 条件字段*/
		String[]whereFieldsArr=whereFields.split(",");
		  int whereFieldLength=whereFieldsArr.length;
		  for (int i = 0; i < whereFieldsArr.length; i++) {
			String whereField = whereFieldsArr[i];
			sb.append(whereField).append("=? ");
			if(i!=whereFieldLength-1) {
				sb.append(" and ");
			}
		}
		System.out.println(sb.toString());
	}
	/**
	 * 插入语句
	 *  String tableName="sys_user";
	 *	String fields="username,password,token";
	 *	String keyFields="id";
	 *	createInstallSql(tableName,fields,keyFields);
	 * @param tableName    表名
	 * @param fields       可为null字段
	 * @param keyFields    必传字段
	 */
	private static void createInstallSql(String tableName, String fields,String keyFields) {
		StringBuffer sb=new StringBuffer("INSERT INTO ");
		sb.append(tableName).append("(").append("\n");
		String[]fieldArr=null;
		if(!StringUtils.isEmpty(fields)) {
			fieldArr=fields.split(",");
		    for (String field : fieldArr) {
		    	 sb.append("<#if ").append(field).append("??> \n");
		    	 sb.append(field).append(",\n");
		    	 sb.append("</#if> \n");
			}
		}
	    String[]keyFieldArr=keyFields.split(",");
	    int length=keyFieldArr.length;
	    for (int i = 0; i < length; i++) {
	    	sb.append(keyFieldArr[i]);	
	    	if(i!=length-1) {
	    		sb.append(",");
	    	}
		}
	    sb.append(")VALUES( \n");
	    if(!StringUtils.isEmpty(fields)) {
		    for (String field : fieldArr) {
		    	 sb.append("<#if ").append(field).append("??> \n");
		    	 sb.append("?,\n");
		    	 sb.append("</#if> \n");
			}
	    }
	    for (int i = 0; i < length; i++) {
	    	sb.append("?");	
	    	if(i!=length-1) {
	    		sb.append(",");
	    	}
		}
	    sb.append(")");
	    System.out.println(sb.toString());
	}
	
}
