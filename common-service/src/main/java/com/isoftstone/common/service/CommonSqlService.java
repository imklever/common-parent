package com.isoftstone.common.service;

import java.util.List;
import java.util.Map;


import com.isoftstone.common.PageInfo;
public interface CommonSqlService {
    List<Map<String, Object>> getSqlQuery(String sql, String params,String dataSource);

    int updateSql(String sql, String params,String dataSource);

    int addSql(String sql, String params,String dataSource);
    
    int addSqlAndGetId(String sql, String params,String dataSource);

    int delSql(String sql, String params,String dataSource);

    PageInfo<Map<String, Object>> getSqlQueryByPage(String sql, Integer page, Integer size, List<String> sort,
            String params,String dataSource);
    
    List<Map<String, Object>> getEsSqlQuery(String sql);
    
    PageInfo<Map<String, Object>> getEsSqlQueryByPage(String sql, Integer page, Integer size);
}
