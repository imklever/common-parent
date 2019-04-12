package com.isoftstone.common.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.isoftstone.common.PageInfo;
import com.isoftstone.common.datasource.DynamicDataSourceContextHolder;
import com.isoftstone.common.datasource.DynamicRoutingDataSource;
import com.isoftstone.common.mapper.CommonSqlMapper;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.util.JdbcEsUtil;

@Service
//@Transactional
public class CommonSqlServiceImpl implements CommonSqlService{
	@Autowired
	CommonSqlMapper commonSqlMapper;
	@Autowired
	DynamicRoutingDataSource dynamicDataSource;
	@Autowired
	JdbcEsUtil jdbcEsUtil;
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public List<Map<String, Object>> getSqlQuery(String sql, String params,String dataSource) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(params)){
			map=JSONObject.parseObject(params);
		}
		map.put("IOC_SQL", sql);
		if(!StringUtils.isEmpty(dataSource)) {
			 DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
		}
		List<Map<String, Object>>list=null;
		try {
			list=commonSqlMapper.getSqlQuery(map);
		} catch (Exception e) {
			throw e;
		}finally {
			if(!StringUtils.isEmpty(dataSource)) {
				  DynamicDataSourceContextHolder.clearDataSourceKey();
			}
		}
		hasClob(list);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getEsSqlQuery(String sql) {
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcEsUtil.queryData(sql);
		return list;
	}
    
	@Override
	public int updateSql(String sql, String params,String dataSource) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(params)){
			map=JSONObject.parseObject(params);
		}
		map.put("IOC_SQL", sql);
		if(!StringUtils.isEmpty(dataSource)) {
			 DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
		}
		int conut=0;
		try {
			 conut=commonSqlMapper.getUpdateSql(map);
		} catch (Exception e) {
			throw e;
		}finally {
			if(!StringUtils.isEmpty(dataSource)) {
				  DynamicDataSourceContextHolder.clearDataSourceKey();
			}
		}
		return conut;
	}

	@Override
	public int addSql(String sql, String params,String dataSource) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(params)){
			map=JSONObject.parseObject(params);
		}
		map.put("IOC_SQL", sql);
		if(!StringUtils.isEmpty(dataSource)) {
			 DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
		}
		int conut=0;
		try {
			 conut=commonSqlMapper.getInsertSql(map);
		} catch (Exception e) {
			throw e;
		}finally {
			if(!StringUtils.isEmpty(dataSource)) {
				  DynamicDataSourceContextHolder.clearDataSourceKey();
			}
		}
		return conut;
	}

	@Override
	public int delSql(String sql, String params,String dataSource) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(params)){
			map=JSONObject.parseObject(params);
		}
		if(!StringUtils.isEmpty(dataSource)) {
			 DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
		}
		map.put("IOC_SQL", sql);
		int conut=0;
		try {
			 conut=commonSqlMapper.getDeleteSql(map);
		} catch (Exception e) {
			throw e;
		}finally {
			if(!StringUtils.isEmpty(dataSource)) {
				  DynamicDataSourceContextHolder.clearDataSourceKey();
			}
		}
		return conut;
	}

	@Override
	public PageInfo<Map<String, Object>> getSqlQueryByPage(String sql, 
			Integer pageNo, Integer pageSize, List<String> sort,
			String params,String dataSource) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(!StringUtils.isEmpty(params)){
			map=JSONObject.parseObject(params);
		}
		if(!StringUtils.isEmpty(dataSource)) {
			 DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
		}
		map.put("IOC_SQL", sql);
		if(!StringUtils.isEmpty(dataSource)) {
			  DynamicDataSourceContextHolder.clearDataSourceKey();
		}
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>>list=null;
		try {
			list=commonSqlMapper.getSqlQuery(map);
		} catch (Exception e) {
			throw e;
		}finally {
			if(!StringUtils.isEmpty(dataSource)) {
				  DynamicDataSourceContextHolder.clearDataSourceKey();
			}
		}
		
		hasClob(list);
		return new PageInfo<Map<String, Object>>(list);
	}
	
	@Override
	public PageInfo<Map<String, Object>> getEsSqlQueryByPage(String sql,
			Integer pageNo, Integer pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list=jdbcEsUtil.queryData(sql);
		return new PageInfo<Map<String, Object>>(list);
	}
	
	private void hasClob(List<Map<String, Object>> list) {
		boolean flag=false;
		if(list!=null&&list.size()>0) {
			Map<String, Object> rowMap=list.get(0);
			for (String clm : rowMap.keySet()) {
				if(rowMap.get(clm) instanceof byte[]) {
					flag=true;
				}if(rowMap.get(clm) instanceof Clob) {
					flag=true;
				}else if(rowMap.get(clm) instanceof Date) {
					flag=true;
				}else if(rowMap.get(clm) instanceof Timestamp) {
					flag=true;
				}/*else if(rowMap.get(clm) instanceof TIMESTAMP) {
					flag=true;
				}*/
			}
		}
		if(flag) {
			for (Map<String, Object> rowMap : list) {
				for (String clm: rowMap.keySet()) {
					try {
						if(rowMap.get(clm) instanceof Clob) {
							rowMap.put(clm,ClobToString((Clob)rowMap.get(clm)));
						}else if(rowMap.get(clm) instanceof Date) {
							rowMap.put(clm,sdf.format(rowMap.get(clm)));
						}else if(rowMap.get(clm) instanceof Timestamp) {
							rowMap.put(clm,sdf.format(rowMap.get(clm)));
						}/*else if(rowMap.get(clm) instanceof TIMESTAMP) {
							rowMap.put(clm,sdf.format(getOracleTimestamp(rowMap.get(clm))));
						}*/else if(rowMap.get(clm) instanceof byte[]) {
							rowMap.put(clm,new String((byte[])rowMap.get(clm)));
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}

	private Date getOracleTimestamp(Object value) {
		try {
			Class clz = value.getClass();
			Method m = clz.getMethod("timestampValue", null);
			return (Timestamp) m.invoke(value, null);
		} catch (Exception e) {
			return null;
		}
	}

	private Object ClobToString(Clob clob) throws SQLException, IOException {
		 String reString = "";   
	        java.io.Reader is = clob.getCharacterStream();// 寰楀埌娴�   
	        BufferedReader br = new BufferedReader(is);   
	        String s = br.readLine();   
	        StringBuffer sb = new StringBuffer();   
	        while (s != null) {// 鎵ц寰幆灏嗗瓧绗︿覆鍏ㄩ儴鍙栧嚭浠樺�肩粰StringBuffer鐢盨tringBuffer杞垚STRING   
	            sb.append(s);   
	            s = br.readLine();   
	        }   
	        reString = sb.toString();   
	        return reString;  
	}

    @Override
    public int addSqlAndGetId(String sql, String params, String dataSource) {
        Map<String, Object> map=new HashMap<String, Object>();
        if(!StringUtils.isEmpty(params)){
            map=JSONObject.parseObject(params);
        }
        map.put(CommonConstants.MAP_SQL, sql);
        if(!StringUtils.isEmpty(dataSource)) {
             DynamicDataSourceContextHolder.setDataSourceKey(dataSource);
        }
        int conut=0;
        try {
             conut=commonSqlMapper.getInsertSqlAndId(map);
        } catch (Exception e) {
            throw e;
        }finally {
            if(!StringUtils.isEmpty(dataSource)) {
                  DynamicDataSourceContextHolder.clearDataSourceKey();
            }
        }
        if(map.containsKey(CommonConstants.MAP_SQL_ADD_ID)) {
            return Integer.parseInt(map.get(CommonConstants.MAP_SQL_ADD_ID).toString());
        }else {
            throw new RuntimeException("鎻掑叆鑷鏃犺繑鍥�!");
        }
    }

}
