package com.isoftstone.common.backup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.common.constant.CommonConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import com.isoftstone.common.plugins.visua.vo.VisuaActionVo;
import com.isoftstone.common.plugins.visua.vo.VisuaContextVo;
import com.isoftstone.common.plugins.visua.vo.VisuaFlowVo;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SpringUtil;
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowTest {
	@Autowired
    JsonService jsonService;
	 private static ExecutorService threadPool = Executors.newFixedThreadPool(20);
	
	public void testAction() throws Exception{
		VisuaActionVo visuaActionVo=new VisuaActionVo();
		Map<String,Object>mapa=new HashMap<String, Object>();
	    mapa.put("list", "a232");
	    mapa.put("name", "3123name21");
	    mapa.put("start", true);
	    mapa.put("end", true);
		visuaActionVo.setParams(jsonService.toJson(mapa));
		visuaActionVo.setObjClass("com.isoftstone.common.action.impl.StartVisuaActionServiceImpl");
		//System.out.println(jsonService.toJson(visuaActionVo));
		/*Class stuClass=Class.forName(visuaActionVo.getObjClass());
        Object obj=SpringUtil.getBean(stuClass);
        for (String key : mapa.keySet()) {
        	Field fied=ReflectionUtils.findField(stuClass, key);
        	if(fied!=null) {
        		fied.set( obj,mapa.get(key));
        	}
	   }
	   Map<String,Object>map=new HashMap<String, Object>();
	   map.put("key", "value");
	   Method thismethod=ReflectionUtils.findMethod(stuClass, "execute",Map.class);
	   ReflectionUtils.invokeMethod(thismethod, obj, map);*/
	}
	@Test
	public void test() throws Exception {
		VisuaContextVo dataMap=new VisuaContextVo();
	    buildNose(dataMap);//构建前端参数
	    List<VisuaActionVo> visuaActionVos= buildAction();//节点解析
	    List<VisuaFlowVo>  visuaFlowVos=buildFlows();//流程
	    Map<String, VisuaActionVo>transformMap= buildActionMap(visuaActionVos);//节点封装成map
		
	    Map<String,Object> flowsmap=buildFlowsMap(visuaFlowVos);
	    Map<String,List<VisuaFlowVo>> preflowMap=
	    		(Map<String, List<VisuaFlowVo>>) flowsmap.get(CommonConstants.FLOW_PRE);//前置流程
	    System.out.println(jsonService.toJson(preflowMap));
	    Map<String,List<VisuaFlowVo>> nextflowMap=
	    		(Map<String, List<VisuaFlowVo>>) flowsmap.get(CommonConstants.FLOW_NEXT);//后置流程
	    System.out.println(jsonService.toJson(nextflowMap));
	    Set<String> startSet=(Set<String>) flowsmap.get(CommonConstants.FLOW_START);
	    System.out.println(jsonService.toJson(startSet));
	    for (String transform : startSet) {
			  System.out.println(transform+"000000000");
			  runNextAction(preflowMap,nextflowMap,transformMap,transform,dataMap);
	    }
	    
	}
	private Map<String,Object> buildFlowsMap(List<VisuaFlowVo> visuaFlowVos) {
		Map<String,List<VisuaFlowVo>>preflowMap=new HashMap<String, List<VisuaFlowVo>>();
		Map<String,List<VisuaFlowVo>>nextflowMap=new HashMap<String, List<VisuaFlowVo>>();
		Set<String>nextSet=new HashSet<>();
		Set<String> preSet=new HashSet<>();
		List<VisuaFlowVo> list=null;
		for (VisuaFlowVo visuaFlowVo : visuaFlowVos) {
			nextSet.add(visuaFlowVo.getNextName());
			preSet.add(visuaFlowVo.getPreName());
			if(nextflowMap.containsKey(visuaFlowVo.getNextName())) {
				nextflowMap.get(visuaFlowVo.getNextName()).add(visuaFlowVo);
			}else {
				list=new ArrayList<VisuaFlowVo>();
				list.add(visuaFlowVo);
				nextflowMap.put(visuaFlowVo.getNextName(), list);
			}
			if(preflowMap.containsKey(visuaFlowVo.getPreName())) {
				preflowMap.get(visuaFlowVo.getPreName()).add(visuaFlowVo);
			}else {
				list=new ArrayList<VisuaFlowVo>();
				list.add(visuaFlowVo);
				preflowMap.put(visuaFlowVo.getPreName(), list);
			}
		}
		Map<String, Object>map=new HashMap<String, Object>();
		boolean aa=preSet.removeAll(nextSet);
		map.put(CommonConstants.FLOW_START, preSet);
		map.put(CommonConstants.FLOW_PRE, preflowMap);
		map.put(CommonConstants.FLOW_NEXT, nextflowMap);
		return map;
				
	}
	private Map<String, VisuaActionVo> buildActionMap(List<VisuaActionVo> visuaActionVos) {
		 Map<String, VisuaActionVo>transformMap=new HashMap<>();
		    for (VisuaActionVo visuaActionVo : visuaActionVos) {
		    	if(transformMap.containsKey(visuaActionVo.getName())) {
		    		throw new RuntimeException("已存在的名称,执行名称不能重复！");
		        }
		    	transformMap.put(visuaActionVo.getName(), visuaActionVo);
			}
		return transformMap;
	}
	private List<VisuaFlowVo> buildFlows() {
		String visuaFlowVoJso="["
				+ "{\"condition\":\"condition\",\"name\":\"flow_001\",\"preName\":\"开始\",\"type\":\"type\",\"nextName\":\"查询sql\"},"
				+ "{\"condition\":\"condition\",\"name\":\"flow_002\",\"preName\":\"查询sql\",\"type\":\"type\",\"nextName\":\"结束\"},"
				+ "{\"condition\":\"condition\",\"name\":\"flow_003\",\"preName\":\"开始\",\"type\":\"type\",\"nextName\":\"查询sql2\"},"
				+ "{\"condition\":\"condition\",\"name\":\"flow_005\",\"preName\":\"查询sql2\",\"type\":\"type\",\"nextName\":\"查询sql3\"},"
				+ "{\"condition\":\"condition\",\"name\":\"flow_004\",\"preName\":\"查询sql3\",\"type\":\"type\",\"nextName\":\"结束\"}"
				+ "]";
		List<VisuaFlowVo>  visuaFlowVos=jsonService.parseArray(visuaFlowVoJso, VisuaFlowVo.class);
		return visuaFlowVos;
	}
	/**
	 * 构建action
	 * @return
	 */
	private List<VisuaActionVo> buildAction() {
		String visuaActionVoJson="["
				+ "{\"name\":\"开始\",\"objClass\":\"com.isoftstone.common.action.impl.StartVisuaActionServiceImpl\",\"params\":\"{\\\"name\\\":\\\"开始\\\",\\\"list\\\":\\\"a232\\\",\\\"start\\\":true}\"},"
				+ "{\"name\":\"查询sql\",\"objClass\":\"com.isoftstone.common.action.impl.SelSqlVisuaActionServiceImpl\",\"params\":\"{\\\"name\\\":\\\"查询sql\\\",\\\"sql\\\":\\\"select * from visua_sql_example limit 3\\\",\\\"isReturn\\\":1}\"},"
				+ "{\"name\":\"查询sql3\",\"objClass\":\"com.isoftstone.common.action.impl.SelSqlVisuaActionServiceImpl\",\"params\":\"{\\\"name\\\":\\\"查询sql3\\\",\\\"sql\\\":\\\"select * from visua_sql_example limit 2\\\",\\\"isReturn\\\":1}\"},"
				+ "{\"name\":\"查询sql2\",\"objClass\":\"com.isoftstone.common.action.impl.SelSqlVisuaActionServiceImpl\",\"params\":\"{\\\"name\\\":\\\"查询sql2\\\",\\\"sql\\\":\\\"select * from visua_sql_example limit 1\\\",\\\"isReturn\\\":1}\"},"
				+ "{\"name\":\"结束\",\"objClass\":\"com.isoftstone.common.action.impl.EndVisuaActionServiceImpl\",\"params\":\"{\\\"name\\\":\\\"结束\\\",\\\"list\\\":\\\"a232\\\",\\\"end\\\":true}\"}"
				+ "]";
		return jsonService.parseArray(visuaActionVoJson, VisuaActionVo.class);
	}
	/**
	 * 构建前台参数
	 * @param dataMap
	 */
	private void buildNose(VisuaContextVo dataMap) {
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("test", "TEST");
		dataMap.put(CommonConstants.CONTEXT_NOSE, map);
	}
	private String runNextAction(Map<String, List<VisuaFlowVo>> preflowMap,
			Map<String, List<VisuaFlowVo>> nextflowMap, Map<String, VisuaActionVo> transformMap,
			String transform, VisuaContextVo  context) throws InterruptedException, ExecutionException {
			String rd="-1";
			List<VisuaFlowVo> visuaFlowVos=nextflowMap.get(transform);
			if(visuaFlowVos!=null) {
				System.out.println(jsonService.toJson(visuaFlowVos));
				for (VisuaFlowVo visuaFlowVo : visuaFlowVos) {
					  VisuaActionVo visuaActionVo= transformMap.get(visuaFlowVo.getPreName());
					  if(visuaActionVo!=null&&visuaActionVo.getResult()!=1) {
						  return rd; 
					  }
				}
			}
		    VisuaActionVo visuaActionVo= transformMap.get(transform);
			Future<String> resut= threadPool.submit(new Callable<String>() {
				 @Override
				  public String call() throws Exception {
						return runTransform(visuaActionVo,context);
				 }
			});
			if(resut.get().equals("0")) {//结果运行完毕
				 visuaActionVo.setResult(1);
				List<VisuaFlowVo> nextVisuaFlowVos=preflowMap.get(transform);
				if(nextVisuaFlowVos!=null) {
					 for (VisuaFlowVo visuaFlowVo : nextVisuaFlowVos) {
						 Future<String> resut2= threadPool.submit(new Callable<String>() {
							 @Override
							  public String call() throws Exception {
									return runNextAction(preflowMap,nextflowMap,transformMap,
								    		visuaFlowVo.getNextName(),
									    	context);
							 }
						});
					  }
				}
			}
		   return rd;
	}
	private String runTransform(VisuaActionVo visuaActionVo, VisuaContextVo context) throws Exception {
		Class stuClass=Class.forName(visuaActionVo.getObjClass());
		Map<String,Object>mapa=jsonService.parseMap(visuaActionVo.getParams());
        Object obj=SpringUtil.getBean(stuClass);
        for (String key : mapa.keySet()) {
        	Field fied=ReflectionUtils.findField(stuClass, key);
        	if(fied!=null) {
        		fied.set(obj,mapa.get(key));
        	}
	    }
 	    Method thismethod=ReflectionUtils.findMethod(stuClass, "execute",VisuaContextVo.class);
 	    return ReflectionUtils.invokeMethod(thismethod, obj, context).toString();
	}
}
