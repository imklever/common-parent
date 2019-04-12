package com.isoftstone.common.backup.service.plugins.visua.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.common.constant.CommonConstants;
import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.isoftstone.common.PageInfo;
import com.isoftstone.common.backup.mapper.plugins.visua.VisuaSqlExampleMapper;
import com.isoftstone.common.backup.service.AbstractBaseService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.backup.support.StartThread;
import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.vo.VisuaSqlInputDataVo;
import com.isoftstone.common.plugins.visua.vo.VisuaSqlVo;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SqlTemplateReplacement;

@Service
public class VisuaSqlExampleServiceImpl extends AbstractBaseService<VisuaSqlExample, String>
		implements VisuaSqlExampleService  {
	@Autowired
	VisuaSqlExampleMapper visuaSqlExampleMapper;
	
	@Autowired
	private JsonService jsonService;
	@Autowired
	SqlTemplateReplacement sqlTemplateReplacement;
	@Autowired
	CommonSqlService commonSqlService;
	@Autowired
	TransactionTemplate transactionTemplate;
	@Autowired
	CommBusinessRunService commBusinessRunService;

	@Override
	protected BaseMapper<VisuaSqlExample, String> getMapper() {
		return visuaSqlExampleMapper;
	}

	@Override
	public VisuaSqlExample getByBusinessCode(String businesscode) {
		return visuaSqlExampleMapper.getByBusinessCode(businesscode);
	}

	@Override
	public int getByBusinessCodeCount(String businessCode) {
		return visuaSqlExampleMapper.getByBusinessCodeCount(businessCode);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getByDataBusinessCode(String businesscode, String params) {
		Map<String, Object> datasMap = new HashMap<String, Object>();
		// 编程式事务
		datasMap = (Map<String, Object>) transactionTemplate.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus arg0) {
				Map<String, Object> datasMap = new HashMap<String, Object>();
				try {
					if("S08044".equals(businesscode)&&"S08102".equals(businesscode)&&"S08101".equals(businesscode)) {
						StartThread startThread = new StartThread();
						startThread.setBusinesscode(businesscode);
						startThread.setParams(params);
						startThread.setVisuaSqlExampleMapper(visuaSqlExampleMapper);
						startThread.setCommonSqlService(commonSqlService);
						startThread.setJsonService(jsonService);
						startThread.setSqlTemplateReplacement(sqlTemplateReplacement);
						startThread.start();
						datasMap = getByParamBusinessCode(businesscode, params);
					}else {
					datasMap = getByParamBusinessCode(businesscode, params);
					}
				} catch (Exception e) {
					System.out.println("事务报错--");
					e.printStackTrace();
					datasMap.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
					arg0.setRollbackOnly();
				}
				return datasMap;
			}
		});

		return datasMap;
	}
	
	private  Map<String, Object> getByParamBusinessCode(String businesscode, String params) throws Exception {
		VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(businesscode);
		return getByParamBusinessCode( visuaSqlExample,  params,null,null);
	}

	private Map<String, Object> getByParamBusinessCode(VisuaSqlExample visuaSqlExample, String params,String name,String type) throws Exception {
        return commBusinessRunService.getByParamBusinessCode(visuaSqlExample, params, name, type);
	}

	@Override
	public List<VisuaSqlExample> findBySel(String visuaSqlExample, List<String> sort) {
		VisuaSqlExample visuaSqlExamples = jsonService.parseObject(visuaSqlExample, VisuaSqlExample.class);
		return visuaSqlExampleMapper.findBy(visuaSqlExamples, sort);
	}

	@Override
	public PageInfo<Map<String, Object>> findBusinessCodeDataByPage(String businesscode, String params, Integer pageNo,
			Integer pageSize, List<String> sort) {

		VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(businesscode);
		String sqlLists = visuaSqlExample.getSqlTemplates();
		Map<String, Object> paramsMap = new HashMap<String, Object>();// 参数Map
		List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlLists, VisuaSqlVo.class);// sql语句解析
		sqlTemplateReplacement.createReplaceMap(paramsMap, visuaSqlExample.getInputData(), params);// 构建参数
		List<Map<String, Object>> list = null;
		PageHelper.startPage(pageNo, pageSize);
		for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
			String sql = null;
			try {
				sql = sqlTemplateReplacement.templateReplacement(paramsMap, visuaSqlVo.getValue());
				System.out.println("sql为"+sql);
			} catch (Exception e) {
				System.err.println(e.getStackTrace());
			}
			if (StringUtils.isEmpty(sql.trim())) {
				continue;
			}
			if (visuaSqlVo.getIsReturn() == 1) {// 数据是否返回
				if (CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {// 查
					list = commonSqlService.getSqlQuery(sql, params, visuaSqlVo.getDataSource());
					System.out.println("list为:"+list);
				} else {// 查
					list = commonSqlService.getSqlQuery(sql, params, visuaSqlVo.getDataSource());
					System.out.println("list为:"+list);
				}
			}else {
				list = commonSqlService.getSqlQuery(sql, params, visuaSqlVo.getDataSource());
				 paramsMap.put(visuaSqlVo.getName(), list);
			}
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		pageInfo.setVisuaInput(visuaSqlExample.getInputData());
		pageInfo.setVisuaOutput(visuaSqlExample.getOutputData());
		pageInfo.setSupportToDo(visuaSqlExample.getSupportToDo());
		pageInfo.setUdfToDo(visuaSqlExample.getUdfToDo());
		return pageInfo;
	}

	@Override
	public PageInfo<VisuaSqlExample> findByPage(String visuaSqlExample, Integer pageNo, Integer pageSize,
			List<String> sort) {
		PageHelper.startPage(pageNo, pageSize);
		VisuaSqlExample visuaSqlExamples = jsonService.parseObject(visuaSqlExample, VisuaSqlExample.class);
		List<VisuaSqlExample> visuaSqlExamplessdas = visuaSqlExampleMapper.findBy(visuaSqlExamples, sort);
		return new PageInfo<VisuaSqlExample>(visuaSqlExamplessdas);
	}

	@Override
	public Map<String, Object> getDataByOneToMany(String businessCode, String params) {
		VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(businessCode);
		String sqlLists = visuaSqlExample.getSqlTemplates();
		Map<String, Object> datasMap = new HashMap<String, Object>();
		datasMap.put(CommonConstants.VISUA_OUTPUT, visuaSqlExample.getOutputData());
		datasMap.put(CommonConstants.VISUA_INPUT, visuaSqlExample.getInputData());
		List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlLists, VisuaSqlVo.class);// sql语句解析
		Map<String, Object> paramsMap = new HashMap<String, Object>();// 参数Map
		sqlTemplateReplacement.createReplaceMap(paramsMap, visuaSqlExample.getInputData(), params);
		// 构建参数--进行mybais的防止注入
		List<Map<String, Object>> list = null;
		Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
		for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
			if (CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {
				try {
					if (visuaSqlVo.getIsReturn() == 1) {
						list = runSql(visuaSqlVo, paramsMap, params);
						returnMap.put(visuaSqlVo.getName(), list);
					} else {
						for (String key : returnMap.keySet()) {
							List<Map<String, Object>> dataList = returnMap.get(key);
							for (Map<String, Object> map : dataList) {
								paramsMap.put(key, map);
								list = runSql(visuaSqlVo, paramsMap, params);
								map.put(visuaSqlVo.getName(), list);
							}
						}
					}
				} catch (Exception e) {
					datasMap.put(ErrorCodeConstants.HASH_ERR,
							visuaSqlVo.getName() + visuaSqlVo.getValue() + "SQL模板替换有误>>" + e.getMessage());
				}
			}
		}
		datasMap.putAll(returnMap);
		return datasMap;
	}

	private List<Map<String, Object>> runSql(VisuaSqlVo visuaSqlVo, Map<String, Object> paramsMap, String params)
			throws Exception {
		List<Map<String, Object>> list = null;
		String sql = null;
		sql = sqlTemplateReplacement.templateReplacement(paramsMap, visuaSqlVo.getValue());
		if (StringUtils.isEmpty(sql.trim())) {
			return null;
		}
		if (CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {// 查
			list = commonSqlService.getSqlQuery(sql, params, visuaSqlVo.getDataSource());
		}
		return list;
	}

	@Override
	public List<VisuaSqlExample> findByUserId(String userId) {
		return visuaSqlExampleMapper.findByUserId(userId);
	}

	@Override
	public PageInfo<Map<String, Object>> getDataByOneToManyByPage(String businessCode, String params, Integer pageNo,
			Integer pageSize, List<String> sort) {

		VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(businessCode);
		String sqlLists = visuaSqlExample.getSqlTemplates();
		Map<String, Object> datasMap = new HashMap<String, Object>();
		datasMap.put(CommonConstants.VISUA_OUTPUT, visuaSqlExample.getOutputData());
		datasMap.put(CommonConstants.VISUA_INPUT, visuaSqlExample.getInputData());
		List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlLists, VisuaSqlVo.class);// sql语句解析
		Map<String, Object> paramsMap = new HashMap<String, Object>();// 参数Map
		sqlTemplateReplacement.createReplaceMap(paramsMap, visuaSqlExample.getInputData(), params);
		// 构建参数--进行mybais的防止注入
		List<Map<String, Object>> list = null;
		PageInfo<Map<String, Object>> pageInfo = null;
		Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
		for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
			if (CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {
				try {
					if (visuaSqlVo.getIsReturn() == 1) {
						PageHelper.startPage(pageNo, pageSize);
						list = runSql(visuaSqlVo, paramsMap, params);
						pageInfo = new PageInfo<Map<String, Object>>(list);
						returnMap.put(visuaSqlVo.getName(), list);
					} else {
						for (String key : returnMap.keySet()) {
							List<Map<String, Object>> dataList = returnMap.get(key);
							for (Map<String, Object> map : dataList) {
								paramsMap.put(key, map);
								list = runSql(visuaSqlVo, paramsMap, params);
								map.put(visuaSqlVo.getName(), list);
							}
						}
					}
				} catch (Exception e) {
					datasMap.put(ErrorCodeConstants.HASH_ERR,
							visuaSqlVo.getName() + visuaSqlVo.getValue() + "SQL模板替换有误>>" + e.getMessage());
				}
			}
		}

		pageInfo.setVisuaInput(visuaSqlExample.getInputData());
		pageInfo.setVisuaOutput(visuaSqlExample.getOutputData());
		pageInfo.setSupportToDo(visuaSqlExample.getSupportToDo());
		pageInfo.setUdfToDo(visuaSqlExample.getUdfToDo());
		return pageInfo;
	}
	/**
	 * 测试类型
	 */
	@Override
	public Map<String, Object> testMode(String dtoJson, String name, String type) {
		VisuaSqlExample visuaSqlExample = jsonService.parseObject(dtoJson, VisuaSqlExample.class);
		String inputJson=visuaSqlExample.getInputData();
		String tetsParams=null;
		if(!StringUtils.isEmpty(inputJson) ) {
			  List<VisuaSqlInputDataVo> visuaSqlInputDataVos=jsonService.parseArray(inputJson, VisuaSqlInputDataVo.class);
			  Map<String, String>map=new HashMap<String, String>();
			  for (VisuaSqlInputDataVo visuaSqlInputDataVo : visuaSqlInputDataVos) {
				  map.put(visuaSqlInputDataVo.getName(), visuaSqlInputDataVo.getTestVal());
			  }
			  tetsParams=jsonService.toJson(map);
		}
		String params = tetsParams==null?"{}":tetsParams;
		Map<String, Object> datasMap = 
		// 编程式事务
		(Map<String, Object>)transactionTemplate.execute(new TransactionCallback() {
					@Override
					public Object doInTransaction(TransactionStatus arg0) {
						Map<String, Object> datasMap = new HashMap<String, Object>();
						try {
							datasMap = getByParamBusinessCode(visuaSqlExample, params,name,type);
						} catch (Exception e) {
							datasMap.put(ErrorCodeConstants.HASH_ERR, e.getMessage());
							arg0.setRollbackOnly();
						}
						return datasMap;
					}
				});
		return datasMap;
	}

}
