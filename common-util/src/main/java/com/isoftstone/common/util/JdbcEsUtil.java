package com.isoftstone.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class JdbcEsUtil {
	
	@Autowired
    MyProps myProps;
	
	private List<Connection> conns = new LinkedList<Connection>();
	
	public void createConnect() {
		//获取连接属性
    	String driverClassName = myProps.getDriverClassName();
    	String jdbcUrl = myProps.getJdbcUrl();
    	String username = myProps.getUsername();
    	String password = myProps.getPassword();
    	
    	try {
    		//加载数据库驱动
			Class.forName(driverClassName);
			//创建连接
			for (int i = 0; i < 5; i++) {
				Connection connection = DriverManager.getConnection(jdbcUrl,username,password);
				conns.add(connection);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public List<Map<String, Object>> queryData(String sql) {
        //查询结果返回list
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        if(conns.size()==0) {
        	createConnect();
        }
        try {
        	Connection connection = conns.remove(0);  
        	Statement statement = connection.createStatement();
        	//String sql = "select count(1) from \"packetbeat-6.6.0-2019.03.18\"";
            ResultSet result = statement.executeQuery(sql);
            ResultSetMetaData rsmd = result.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (result.next()) {
            	//System.out.println(result.getInt(1));
    			Map<String,Object> rowData = new HashMap<String,Object>();
    			for (int i = 1; i <= columnCount; i++) {
    				rowData.put(rsmd.getColumnName(i), result.getObject(i));
    			}
    			list.add(rowData);
    		}
            if(result!=null) {
            	result.close();
            }
            if(statement!=null) {
            	statement.close();
            }
            if(connection!=null) {
            	connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
