package com.isoftstone.common.backup.service.gjdw.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isoftstone.common.backup.service.gjdw.GjdwService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.util.JsonService;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Service
public class GjdwServiceImpl implements GjdwService {
	
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;
	@Autowired
	JsonService jsonService;
	
	@Override
	public void setNameList(Map<String,Object> map) {
		System.out.println(jsonService.toJson(map));
		//获取连接信息
	    Map<String, Object> dataMap=getConnectInfo();
		String localip=(String) dataMap.get("localip");
		String username=(String) dataMap.get("username");
		String password=(String) dataMap.get("password");
		//创建连接
		Connection con = new Connection(localip);
		boolean isAuthenticated = false;
		//组装命令
		String sh_type=(String) map.get("sh_type");
   		String host_ip=(String) map.get("host_ip");
   		//String hostid=(String) map.get("hostid");
   		String command1="zabbix_get -s "+host_ip+" -k system.run";
   		String command2=null;
   		/*
   		 * sh_type的值为INIT时初始化，为SET时设置黑白名单，为DEL时删除黑白名单
   		 * SET和DEL时具体是黑还是白根据初始化时的值决定
   		 */
	    if(sh_type.equals("INIT")) {
	    	String init_value=(String) map.get("init_value");
	    	//查询脚本地址
	    	Map<String, Object> param = new HashMap<String, Object>();
	    	param.put("sh_type", "INIT");
	    	param.put("bl_or_wh", init_value);
	    	Map<String, Object> datas=visuaSqlExampleService.getByDataBusinessCode("P06061", 
	    			jsonService.toJson(param));
	    	if(datas!=null && datas.containsKey("datalist")) {
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
				Map<String, Object> valueMap = list.get(0);
				String url=(String) valueMap.get("url");
				String sh_name=(String) valueMap.get("sh_name");
				command2="[\""+url+sh_name+" "+init_value+"\"]";
			}
	    }else if(sh_type.equals("SET")) {
	    	String list_type=(String) map.get("list_type");
	    	String list_ip=(String) map.get("list_ip");
	    	//查询脚本地址
	    	Map<String, Object> param = new HashMap<String, Object>();
	    	param.put("sh_type", "SET");
	    	param.put("bl_or_wh", list_type);
	    	Map<String, Object> datas=visuaSqlExampleService.getByDataBusinessCode("P06061", 
	    			jsonService.toJson(param));
	    	if(datas!=null && datas.containsKey("datalist")) {
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
				Map<String, Object> valueMap = list.get(0);
				String url=(String) valueMap.get("url");
				String sh_name=(String) valueMap.get("sh_name");
				command2="[\""+url+sh_name+" "+list_type+" "+list_ip+"\"]";
			}
	    	/* Map<String, Object> param = new HashMap<String, Object>();
	    	param.put("hostid", hostid);
	    	Map<String, Object> datas=visuaSqlExampleService.getByDataBusinessCode("N06061", 
	    			jsonService.toJson(param));
			if(datas!=null && datas.containsKey("datalist")) {
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
				Map<String, Object> valueMap = list.get(0);
				String init_value=(String) valueMap.get("init_value");
				if(init_value.equals("BL")) {
					command2="[/test/set_white.sh "+list_type+" "+list_ip+"]";
				}else if(init_value.equals("WH")) {
					command2="[/test/set_black.sh "+list_type+" "+list_ip+"]";
				}
			}*/
	    }else if(sh_type.equals("DEL")) {
	    	String list_type=(String) map.get("list_type");
	    	String list_ip=(String) map.get("list_ip");
	    	//查询脚本地址
	    	Map<String, Object> param = new HashMap<String, Object>();
	    	param.put("sh_type", "DEL");
	    	param.put("bl_or_wh", list_type);
	    	Map<String, Object> datas=visuaSqlExampleService.getByDataBusinessCode("P06061", 
	    			jsonService.toJson(param));
	    	if(datas!=null && datas.containsKey("datalist")) {
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
				Map<String, Object> valueMap = list.get(0);
				String url=(String) valueMap.get("url");
				String sh_name=(String) valueMap.get("sh_name");
				command2="[\""+url+sh_name+" "+list_type+" "+list_ip+"\"]";
			}
	    }
	    String command=command1+command2;
		//执行命令
		try {
			con.connect();
			isAuthenticated = con.authenticateWithPassword(username, password);
			System.out.println(isAuthenticated);
			if(isAuthenticated) {
				Session session = con.openSession();
				//session.execCommand("zabbix_get -s "+host_ip+" -k system.run[\" ls / \"]");
				session.execCommand(command);
				System.out.println(command);
				String result = processStdout(session.getStdout());
				System.out.println(result);
				session.close();
				con.close();  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, Object> getConnectInfo() {
		Map<String, Object> datas=visuaSqlExampleService.getByDataBusinessCode("S06066", "{}");
		if(datas!=null && datas.containsKey("datalist")) {
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> list=(List<Map<String, Object>>) datas.get("datalist");
			Map<String, Object> dataMap = list.get(0);
			return dataMap;
		}
		return null;
	}
	
	private String processStdout(InputStream in) {
        InputStream is = new StreamGobbler(in);  
        StringBuffer buffer = new StringBuffer();
        try {  
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));  
            String line = null;  
            while((line=br.readLine()) != null){  
                buffer.append(line+"\n");  
            }  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer.toString();  
    }

}
