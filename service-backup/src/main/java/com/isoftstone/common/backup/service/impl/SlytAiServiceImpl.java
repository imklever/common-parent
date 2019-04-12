package com.isoftstone.common.backup.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.isoftstone.common.backup.service.SlytAiService;

/*import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.isoftstone.ai.slytai.AIInterface;
import com.isoftstone.ai.slytai.APIConstants;
import com.isoftstone.common.BaseDatabaseInfo;
import com.isoftstone.common.backup.controller.plugins.visua.VisuaSqlExampleController;
import com.isoftstone.common.backup.service.SlytAiService;
import com.isoftstone.common.backup.support.SpringUtils;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.util.DateUtil;
import com.isoftstone.common.util.SqlTemplateReplacement;*/
@Service
public class SlytAiServiceImpl implements SlytAiService  {

	/*
	@Autowired
	CommonSqlService commonSqlService;
	@Autowired
	SqlTemplateReplacement sqlTemplateReplacement;
	@Autowired
    VisuaSqlExampleController visuaSqlExampleController;
//	@Autowired
	SpringUtils springUtils;
	
	*//**
	 * 胜利油田数据采集
	 *//*
	@Override
	public void slytAiCaiji(Map<String, Object> paramMap) {
		System.out.println("backup开始执行");
		
//		
		String sql="SELECT id," + 
				"TYPE," + 
				"IF(TYPE=1,'oracle.jdbc.OracleDriver','com.mysql.cj.jdbc.Driver') AS driver," + 
				"IF(TYPE=1,CONCAT('jdbc:oracle:thin:@',ip,':',PORT,':',base_name),CONCAT('jdbc:mysql://',ip,':',PORT,'/',base_name)) AS url," + 
				"username," + 
				"PASSWORD," + 
				"yj_sql," + 
				"sj_sql," + 
				"gt_sql FROM oil_database_info WHERE begin_time =DATE_FORMAT(NOW(),'%H') AND STATUS =1";
		List<Map<String, Object>> sqlQuery = commonSqlService.getSqlQuery(sql, null, null);
		System.out.println(sqlQuery);
		List<BaseDatabaseInfo> list = new ArrayList<BaseDatabaseInfo>();
		if(sqlQuery.size()>0) {
		for (Map<String, Object> map : sqlQuery) {
			BaseDatabaseInfo info = new BaseDatabaseInfo();
			info.setId(map.get("id").toString());
			info.setDriver(map.get("driver").toString());
			info.setDataBaseType(map.get("TYPE").toString());
			info.setUrl(map.get("url").toString());
			info.setUsername(map.get("username").toString());
			info.setPassword(map.get("PASSWORD").toString());
				if(map.containsKey("yj_sql")) {
				 info.setYjSql(map.get("yj_sql").toString());				
			}else {
				info.setYjSql(null);
			}
				if(map.containsKey("sj_sql")) {
				 info.setSjSql(map.get("sj_sql").toString());
			}else {
				info.setSjSql(null);
			}
				if(map.containsKey("gt_sql")) {
				 info.setGtSql(map.get("gt_sql").toString());
			}else {
				info.setGtSql(null);
			}
				list.add(info);
		}
		for (BaseDatabaseInfo info : list) {
//		连接数据库
		springUtils.createJdbcTemplate(info);
//		依次采集水井油井功图的数据
		int sjSuccessNum =0;
		int yjSuccessNum =0;
		int gtSuccessNum =0;
	    int sjTaskId = 0;
	    int yjTaskId = 0;
	    int gtTaskId = 0;
	    String sj ="TRUNCATE TABLE oil_ss_sj";
	    String yj ="TRUNCATE TABLE oil_ss_yj";
	    String gt ="TRUNCATE TABLE oil_ss_gt";
	    commonSqlService.getSqlQuery(sj,null,null);
	    commonSqlService.getSqlQuery(yj,null,null);
	    commonSqlService.getSqlQuery(gt,null,null);
		if(null!=info.getSjSql()) {
			sjTaskId= getTaskId();		
			String add_sql="INSERT INTO oil_task_info(id,base_id,begin_time,end_time,data_type,row_num,LOG) VALUES" + 
					"("+sjTaskId+","+info.getId()+",now(),null,3,"+sjSuccessNum+",null)";
			commonSqlService.addSql(add_sql, null, null);
			int successNum = sjSuccessNum = collectDataCtrl(info.getId(),info.getSjSql(),info.getDataBaseType(),3);
		}
		if(null!=info.getYjSql()) {
			yjTaskId= getTaskId();
			yjSuccessNum = collectDataCtrl(info.getId(),info.getYjSql(),info.getDataBaseType(),2);
			String add_sql="INSERT INTO oil_task_info(id,base_id,begin_time,end_time,data_type,row_num,LOG) VALUES" + 
					"("+sjTaskId+","+info.getId()+",now(),null,2,"+yjSuccessNum+",null)";
			int successNum = commonSqlService.addSql(add_sql, null, null);
		}
		if(null!=info.getGtSql()) {
			gtTaskId= getTaskId();
			gtSuccessNum = collectDataCtrl(info.getId(),info.getGtSql(),info.getDataBaseType(),1);
			String add_sql="INSERT INTO oil_task_info(id,base_id,begin_time,end_time,data_type,row_num,LOG) VALUES" + 
					"("+sjTaskId+","+info.getId()+",now(),null,1,"+yjSuccessNum+",null)";
			int successNum = commonSqlService.addSql(add_sql, null, null);
		}
		HashMap<String, Object> map = new HashMap<>();
		System.out.println("成功采集水井数据"+sjSuccessNum+"成功采集油井数据"+yjSuccessNum+"成功采集功图数据"+gtSuccessNum);
		map.put("table_name", "oil_ss_gt,oil_ss_yj,oil_ss_sj");
		Map<String, Object> slytAiInfo = slytAiInfo(map);
		System.out.println("成功添加得分数据"+slytAiInfo);
		if(null!=info.getSjSql()) {
			String updateSql = "UPDATE  oil_task SET task_end = NOW() where id ="+sjTaskId;
			List<Map<String, Object>> resultMap =  commonSqlService.getSqlQuery(updateSql, null, null);
		}
		if(null!=info.getYjSql()) {
			String updateSql = "UPDATE  oil_task SET task_end = NOW() where id ="+yjTaskId;
			List<Map<String, Object>> resultMap =  commonSqlService.getSqlQuery(updateSql, null, null);
		}
		if(null!=info.getGtSql()) {
			String updateSql = "UPDATE  oil_task SET task_end = NOW() where id ="+gtTaskId;
			List<Map<String, Object>> resultMap = commonSqlService.getSqlQuery(updateSql, null, null);
		}
		}}
	}
	private int collectDataCtrl(String id,String sql,String dataBaseType,int type) {
		System.out.println("链接数据");
		int sucessNum =0;
		JdbcTemplate jdbc = SpringUtils.getSingleBeanByName(id+"_jdbcTemplate");
		List<Map<String, Object>> list = jdbc.queryForList("select JH from "
				+sql.toUpperCase().split("FROM")[1]
				+ " group by JH");
		for (Map<String, Object> map : list) {
			String JH = map.get("JH").toString();
			int pageNo=1;
			int pageSize=1000;
			Boolean isSkip=true;
			while(isSkip) {
				String querySql=null;
				if(sql.toUpperCase().contains("WHERE")) {
					querySql=sql + "AND jh=" + "'"+JH+"'";	
				}else {
					querySql=sql + "WHERE jh=" + "'"+JH+"'";
				}				
				String newSql = sqlConCat(querySql,pageSize,pageNo,dataBaseType);
				System.out.println(newSql);
				List<Map<String, Object>> rows = jdbc.queryForList(newSql);
				System.out.println("获取数据"+rows.size()+"等待入库");
//				入库本地数据库
				HashMap<String, Object> addMap = new HashMap<String,Object>();
				addMap.put("dataList", rows);
				String addSql="";
//				数据分类  1 油井功图数据 2油井压力温度数据 3水井压力温度数据
				if(type==1) {
					addSql="<#if dataList??&&(dataList?size>0)>  insert into oil_ss_gt(jh,cjsj,WY,ZH,DGQX,CC,QK)  values<#list dataList as data>  ('${data['JH']}','${data['CJSJ']}','${data['WY']}','${data['ZH']}','${data['DGQX']}','${data['CC']}','${data['QK']}') <#if (data_index+1 != dataList?size)>,</#if>  </#list>  </#if>";
				}else if(type==2) {
					addSql="<#if dataList??&&(dataList?size>0)>  insert into oil_ss_yj(jh,cjsj,jkyy,jkwd,QK)  values<#list dataList as data>  ('${data['JH']}','${data['CJSJ']}','${data['JKYY']}','${data['JKWD']}','${data['QK']}') <#if (data_index+1 != dataList?size)>,</#if>  </#list>  </#if>";	
				}else {
					addSql="<#if dataList??&&(dataList?size>0)>  insert into oil_ss_sj(jh,cjsj,ZSYL1,LJLL,QK)  values<#list dataList as data>  ('${data['JH']}','${data['CJSJ']}','${data['ZSYL1']}','${data['LJLL']}','${data['QK']}') <#if (data_index+1 != dataList?size)>,</#if>  </#list>  </#if>";
				}
				try {
					addSql = sqlTemplateReplacement.templateReplacement(addMap,addSql);
//					System.out.println(addSql);
					sucessNum=sucessNum+commonSqlService.addSql(addSql, null, null);
					System.out.println(sucessNum);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageNo++;
				if(rows.size()<pageSize) {
					isSkip=false;
				}
			}
		}
		return sucessNum;
	}
//	sql拼接
	public String sqlConCat(String sql,int pageSize,int pageNo,String dataBaseType) {
		String newSql =null;
//		1:oracle2mysql
		if("1".equals(dataBaseType)) {
			String[] split = sql.toUpperCase().split("FROM");
			newSql = "SELECT *" + 
					" FROM ("+split[0]+",ROWNUM AS ROWNO FROM"+split[1]+" AND ROWNUM <="
					+ pageSize*pageNo+") TABLE_ALIAS" + 
							" WHERE TABLE_ALIAS.ROWNO > "
							+ pageSize*(pageNo-1);
		}else if("2".equals(dataBaseType)) {
			int offSet = pageSize*(pageNo-1);
			newSql=sql+" limit "+offSet+","+pageSize;
		}
		return newSql;
	}
	*//**
	 * 胜利油田Ai明细得分计算
	 *//*
	@Override
	public Map<String, Object> slytAiInfo(Map<String, Object> map) {
		String tableNamelist = map.get("table_name").toString();
		String updateSqlbegin ="UPDATE oil_table_info SET begintime = NOW(),status = 2 WHERE table_name ='"+tableNamelist+"'";
		commonSqlService.updateSql(updateSqlbegin, null, null);
		String[] split = tableNamelist.split(",");
		HashMap<String, Object> successMap = new HashMap<>();
		for (String  tableName: split) {
			//清空 oil_sensor_data_score
			commonSqlService.getSqlQuery("TRUNCATE TABLE oil_sensor_data_score", null, null);
			String upperCase = tableName.toUpperCase();
			if(upperCase.contains("YJ")){
				yjAi(successMap,tableName);
			}else if(upperCase.contains("SJ")) {
				sjAi(successMap,tableName);
			}else if(upperCase.contains("GT")) {
				gtAi(successMap,tableName);
			}
		}
		if(!successMap.isEmpty()) {
//			执行计算
			slytAiJvHe(map);
			String updateSqlend ="UPDATE oil_table_info SET endtime = NOW(),status = 3 WHERE table_name ='"+tableNamelist+"'";
			commonSqlService.updateSql(updateSqlend, null, null);
			
		}
		System.out.println(successMap);
		return successMap;
	}
	*//**
	 * 胜利油田数据聚合计算
	 *//*
	@Override
	public void slytAiJvHe(Map<String, Object> map) {
		//将可修改为 不可修改
		String updateSqlend ="UPDATE oil_variates SET STATUS =0 WHERE STATUS = 1 AND CONCAT(batch,sensor_id) IN ( SELECT CONCAT(batch,sensor_id) FROM oil_sensor_data_score)";
		int updateSql = commonSqlService.updateSql(updateSqlend, null, null);
		System.out.println("改为不可修改"+updateSql);
//		将可修改改为计算中
		String sql ="UPDATE oil_variates SET STATUS =2 WHERE STATUS = 1 AND  CONCAT(batch,sensor_id) NOT IN ( SELECT CONCAT(batch,sensor_id) FROM oil_sensor_data_score)";
		int sqlnum = commonSqlService.updateSql(sql, null, null);
		System.out.println("改为计算中"+sqlnum);
		Map<String, Object> businessCode49 = visuaSqlExampleController.getByDataBusinessCode("S08049","{}");
		Map<String, Object> businessCode50 = visuaSqlExampleController.getByDataBusinessCode("S08050","{}");
	}	
	*//***
	 * 油井:压力， 温度传感器计算
	 * @param successMap 
	 * @param tableName 表名
	 *//*
	public void yjAi(HashMap<String, Object> successMap, String tableName) {
		String queryJH = "select JH,QK from "+tableName+" group by JH,QK";
		List<Map<String, Object>> jhlist = commonSqlService.getSqlQuery(queryJH, null, null);
		for (Map<String, Object> map : jhlist) {
			String JH = map.get("JH").toString();
			String QK = map.get("QK").toString();
			String queryYj = "SELECT t.JH,REPLACE(CONVERT(t.CJSJ USING ASCII),'???','') AS CJSJ,t.JKYY,t.JKWD,t.QK FROM "+tableName+" t where t.JH ="
					+ "'" + JH + "'"
					+" AND t.QK='"+QK+"'"
					+"AND CHAR_LENGTH(t.cjsj) >16";
			System.out.println("queryYj"+queryYj);
			List<Map<String, Object>> sqlQuery = commonSqlService.getSqlQuery(queryYj, null, null);
			// 每隔10分钟 取 两条 数据，一天的数据组成一行数据 具体到井
			Map<String, List<Map<String, Object>>> findDataMap = findDataInHour(sqlQuery, 12);
			List<List<Object>> listwd = new ArrayList<List<Object>>();
			List<List<Object>> listyy = new ArrayList<List<Object>>();
			for (String key : findDataMap.keySet()) {
				List<Object> lineswd = new ArrayList<Object>();
				List<Object> linesyy = new ArrayList<Object>();
				lineswd.add(key+","+JH+","+QK);
				linesyy.add(key+","+JH+","+QK);
				StringBuffer linewd = new StringBuffer();
				StringBuffer lineyy = new StringBuffer();
				List<Map<String, Object>> list = findDataMap.get(key);
				for (Map<String, Object> newmap : list) {
					if (linewd.length() != 0) {
						linewd.append(",");
					}
					if (lineyy.length() != 0) {
						lineyy.append(",");
					}
					linewd.append(newmap.get("JKWD").toString());
					lineyy.append(newmap.get("JKYY").toString());
				}
				lineswd.add(linewd.toString());
				linesyy.add(lineyy.toString());
				listwd.add(lineswd);
				listyy.add(linesyy);
			}
			try {
				if (listwd.size() > 0) {
					List<Map<String, Double>> scoreswd = AIInterface.getScoresForList(APIConstants.KEY_YOUJING_WENDU, 1,
							listwd);
					System.out.println("AI计算数据"+scoreswd.size());
					int wdscorelines = scoreadd(scoreswd,"2","2");
					System.out.println("wdscorelines"+wdscorelines);
					List<Map<String, Double>> scoresyy = AIInterface.getScoresForList(APIConstants.KEY_YOUJING_YOUYA, 1,
							listyy);
					System.out.println("AI计算数据"+scoresyy.size());
//					得分数据入库
					int yyscorelines = scoreadd(scoresyy,"1","2");
					System.out.println("yyscorelines"+yyscorelines);
					if(successMap.containsKey("YJWD")) {
						int sumwdlines = (int) successMap.get("YJWD")+wdscorelines;
						successMap.put("YJWD", +sumwdlines);
					}else {
						successMap.put("YJWD", wdscorelines);	
					}
					if(successMap.containsKey("YJYY")) {
						int sumyylines = (int) successMap.get("YJYY")+yyscorelines;
					    successMap.put("YJYY", sumyylines);
					}else {
						successMap.put("YJYY", yyscorelines);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void sjAi(HashMap<String, Object> successMap, String tableName) {

		String queryJH = "select JH,QK from "+tableName+" group by JH,QK";
		List<Map<String, Object>> jhlist = commonSqlService.getSqlQuery(queryJH, null, null);
		for (Map<String, Object> map : jhlist) {
			String JH = map.get("JH").toString();
			String QK = map.get("QK").toString();
			String queryYj = "SELECT t.JH,REPLACE(CONVERT(t.CJSJ USING ASCII),'???','') AS CJSJ,t.ZSYL1,t.LJLL,t.QK FROM "+tableName+" t where t.JH ="
					+ "'" + JH + "'"
					+"AND CHAR_LENGTH(t.cjsj) >16";
			System.out.println("querySj"+queryYj);
			List<Map<String, Object>> sqlQuery = commonSqlService.getSqlQuery(queryYj, null, null);
			// 每隔10分钟 取 两条 数据，一天的数据组成一行数据 具体到井
			Map<String, List<Map<String, Object>>> findDataMap = findDataInHour(sqlQuery, 12);
			List<List<Object>> listyl = new ArrayList<List<Object>>();
			List<List<Object>> listll = new ArrayList<List<Object>>();
			for (String key : findDataMap.keySet()) {
				List<Object> linesyl = new ArrayList<Object>();
				List<Object> linesll = new ArrayList<Object>();
				linesyl.add(key+","+JH+","+QK);
				linesll.add(key+","+JH+","+QK);
				StringBuffer lineyl = new StringBuffer();
				StringBuffer linell = new StringBuffer();
				List<Map<String, Object>> list = findDataMap.get(key);
				for (Map<String, Object> newmap : list) {
					if (lineyl.length() != 0) {
						lineyl.append(",");
					}
					if (linell.length() != 0) {
						linell.append(",");
					}
					lineyl.append(newmap.get("ZSYL1").toString());
					linell.append(newmap.get("LJLL").toString());
				}
				linesyl.add(lineyl.toString());
				linesll.add(linell.toString());
				listyl.add(linesyl);
				listll.add(linesll);
			}
			try {
				if (listyl.size() > 0) {
					System.out.println(listyl.size());
					List<Map<String, Double>> scoresyl = AIInterface.getScoresForList(APIConstants.KEY_SHUIJING_YALI, 1,listyl);
					System.out.println("AI计算数据"+scoresyl.size());
					int ylscorelines = scoreadd(scoresyl,"3","3");
					List<Map<String, Double>> scoresll = AIInterface.getScoresForList(APIConstants.KEY_SHUIJING_LIULIANG, 1,listll);
					System.out.println("AI计算数据"+scoresll.size());
					int llscorelines = scoreadd(scoresll,"4","3");
					if(successMap.containsKey("SJYL")) {
						int	sumyllines = (int) successMap.get("SJYL")+ylscorelines;
						successMap.put("SJYL", sumyllines);
					}else {
						successMap.put("SJYL", ylscorelines);	
					}
					if(successMap.containsKey("SJLL")) {
						int sumlllines=(int) successMap.get("SJLL")+llscorelines;
						successMap.put("SJLL", sumlllines);
					}else {
						successMap.put("SJLL", llscorelines);	
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	public void gtAi(HashMap<String, Object> successMap, String tableName) {
		String queryJH = "SELECT JH,QK FROM "+tableName+" GROUP BY JH,QK";
		List<Map<String, Object>> jhlist = commonSqlService.getSqlQuery(queryJH, null, null);
		for (Map<String, Object> map : jhlist) {
			String JH = map.get("JH").toString();
			String QK = map.get("QK").toString();
			String queryGT = "select REPLACE(CONVERT(t.CJSJ USING ASCII),'???','') AS CJSJ,t.JH,t.QK,t.CC,t.WY,t.ZH,t.DGQX from "+tableName+" t where t.JH ="
					+ "'" + JH + "'"
			+"AND CHAR_LENGTH(t.cjsj) >16";
			List<Map<String, Object>> sqlQuery = commonSqlService.getSqlQuery(queryGT, null, null);
			System.out.println("query"+sqlQuery.size());
			try {
				if (sqlQuery.size() > 0) {
					 List<Map<String, Double>> gtscores = AIInterface.getScores(APIConstants.KEY_YOUJING_GONGTU, 3, sqlQuery);
					 System.out.println("AI计算数据"+gtscores.size());
					 int zhscorelines = scoreadd(gtscores,"6","1");
					 if(successMap.containsKey("GTZH")) {
						 int sumzhlines =(int) successMap.get("GTZH")+zhscorelines;
						 successMap.put("GTZH", sumzhlines);
					 }else {
						 successMap.put("GTZH", zhscorelines); 
					 }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Map<String, List<Map<String, Object>>> findDataInHour(List<Map<String, Object>> list, int hourNum) {
		Long previousTime = 0L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, List<Map<String, Object>>> map = new LinkedHashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> secondData : list) {
			Long thisDate = null;
			try {
				//获取时间字段 CJSJ2018-5-1 12:17:07
				thisDate = format.parse((String) secondData.get("CJSJ")).getTime();
				String thisKey = DateUtil.format(new Date(thisDate), "yyyy-MM-dd");
				boolean[] bools = checkDateIsInDayAndHour(thisDate, previousTime);

				if (bools[1]) {// 一小时内
					mapList.add(secondData);
				} else {// 不在一小时内的 ，先随机取出上一个一小时内的数据
					if (bools[0]) {// 一天内的数据继续叠加
						if (map.containsKey(thisKey)) {
							map.get(thisKey).addAll(getListOrderNum(mapList, hourNum));
						} else {
							map.put(thisKey, getListOrderNum(mapList, hourNum));
						}
					}
					mapList = new ArrayList<Map<String, Object>>();
					mapList.add(secondData);
				}
				previousTime = thisDate;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	// 判断是否在一小时内
	*//**
	 * 当前时间和上一个时间的时间戳比较，是否在一天内 和 一小时内
	 * 
	 * @param thisTime
	 * @param previousTime
	 * @return [boolean,boolean] 0:是否在一天内 1:是否在一小时内
	 *//*
	public boolean[] checkDateIsInDayAndHour(Long thisTime, Long previousTime) {
		if (previousTime == 0) {
			return new boolean[] { true, true };
		}
		Date thisDate = new Date(thisTime);
		Date previousDate = new Date(previousTime);
		if (DateUtil.format(thisDate, "yyyy-MM-dd HH").equals(DateUtil.format(previousDate, "yyyy-MM-dd HH"))) {
			return new boolean[] { true, true };
		} else if (DateUtil.format(thisDate, DateUtil.DEFAULT_DATE_FORMAT)
				.equals(DateUtil.format(previousDate, DateUtil.DEFAULT_DATE_FORMAT))) {
			return new boolean[] { true, false };
		} else {
			return new boolean[] { false, false };
		}
	}

	// 在数组里面 按照 取数个数 和 总数的关系，顺序的获取要去个数的数组
	private List<Map<String, Object>> getListOrderNum(List<Map<String, Object>> list, int num) {
		int listSize = list.size();
		if (listSize <= num) {
			return list;
		}
		int index = (listSize - listSize % num) / num;
		List<Map<String, Object>> nlist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < listSize; i++) {
			if (i % index == 0) {
				nlist.add(list.get(i));
			}
			if (nlist.size() == num) {
				break;
			}
		}
		return nlist;
	}
	private int scoreadd(List<Map<String, Double>> scoreswd,String sensor_id,String data_type) {
		Map<String, Object> map1 = new HashMap<String,Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		for (Map<String, Double> scoremap : scoreswd) {
			for (String key : scoremap.keySet()) {
				Map<String, Object> mapinfo = new HashMap<String,Object>();
				String[] split = key.split(",");
				String CJSJ=split[0];
				String jh=split[1];
				String QK=split[2];
				Double score = scoremap.get(key);
				mapinfo.put("data_time", CJSJ);
				mapinfo.put("well_num", jh);
				mapinfo.put("QK", QK);
				mapinfo.put("data_score",score);
				dataList.add(mapinfo);
			}
		}
		map1.put("dataList", dataList);
		String addSql=" <#if dataList??&&(dataList?size>0)> " + 
				" insert into oil_sensor_data_score(well_num,data_time,data_score,sensor_id,data_type,QK) " + 
				" values<#list dataList as data> " + 
				" ('${data['well_num']}','${data['data_time']}','${data['data_score']?c}', "+sensor_id + ","+data_type+
				",'${data['QK']}' ) <#if (data_index+1 != dataList?size)>,</#if> " + 
				" </#list> " + 
				" </#if>";
		try {
			addSql = sqlTemplateReplacement.templateReplacement(map1,addSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int addNum =0;
		if(!StringUtil.isEmpty(addSql)) {
			System.out.println("获取到的sql"+addSql);
			addNum = commonSqlService.addSql(addSql, null, null);
			System.out.println("添加成功数量"+addNum);			
		}
		return addNum; 
	}
	private int getTaskId() {
		int id =1;
		String querySql ="select max(id) as id from oil_task_info";
		List<Map<String, Object>> maxId = commonSqlService.getSqlQuery(querySql, null, null);			
		for (Map<String, Object> map : maxId) {
			if(null!=map.get("id")) {
				int b = Integer.parseInt(map.get("id").toString());
				id = b;
			}		
		}
		return id;
	}
*/

	@Override
	public void slytAiCaiji(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> slytAiInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void slytAiJvHe(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}}