package com.isoftstone.common.service.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.constant.CommonConstants;
import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.ReflexClassDto;
import com.isoftstone.common.common.MongoSqlDto;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.vo.VisuaSqlVo;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.service.CommonMongoService;
import com.isoftstone.common.service.CommonSqlService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SpringUtil;
import com.isoftstone.common.util.SqlTemplateReplacement;
@Service
public class CommBusinessRunServiceImpl implements CommBusinessRunService{
    @Autowired
    SqlTemplateReplacement sqlTemplateReplacement;
    @Autowired
    private JsonService jsonService;
    @Autowired
    CommonSqlService commonSqlService;
    @Autowired
    CommonMongoService commonMongoService;

    @Override
    public Map<String, Object> getByParamBusinessCode(VisuaSqlExample visuaSqlExample,
            String params, String name,
            String type) throws Exception {
        boolean flag=false;
        /**
         * 判断是否是测试
         */
        if(!StringUtils.isEmpty(name)&&!StringUtils.isEmpty(type)) {
            flag=true;
        }
        
        String sqlLists = visuaSqlExample.getSqlTemplates();
        int versionNo=visuaSqlExample.getVersionNo();
        
        Map<String, Object> datasMap = new HashMap<String, Object>();
        datasMap.put(CommonConstants.VISUA_OUTPUT, visuaSqlExample.getOutputData());
        datasMap.put(CommonConstants.VISUA_INPUT, visuaSqlExample.getInputData());

        Map<String, Object> paramsMap = new HashMap<String, Object>();// 参数Map
        Map<String, Object> realParamsMap = jsonService.parseMap(params);//真实参数Map

        List<VisuaSqlVo> visuaSqlVos = jsonService.parseArray(sqlLists, VisuaSqlVo.class);// sql语句解析
        sqlTemplateReplacement.createReplaceMap(versionNo,paramsMap, visuaSqlExample.getInputData(), params,flag);// 构建参数
        for (VisuaSqlVo visuaSqlVo : visuaSqlVos) {
            String sql =  visuaSqlVo.getValue();
            System.out.println(sql);
            try {
                if(CommonConstants.VISUA_TYPE_MONGO_SEL.equals(visuaSqlVo.getType())
                    ||CommonConstants.VISUA_TYPE_MONGO_DEL.equals(visuaSqlVo.getType())
                        ||CommonConstants.VISUA_TYPE_MONGO_ADD.equals(visuaSqlVo.getType())
                           ||CommonConstants.VISUA_TYPE_REFLEX.equals(visuaSqlVo.getType())
                            ||CommonConstants.VISUA_TYPE_ECHARTS.equals(visuaSqlVo.getType())) {
                    sql = sqlTemplateReplacement.templateReplacement(realParamsMap,sql);
                }else {
                    sql = sqlTemplateReplacement.templateReplacement(paramsMap,sql);
                }
                if(flag&&visuaSqlVo.getName().equals(name)
                        &&CommonConstants.TEST_MODE_TYPE_SQL.equals(type)
                        ) {//测试替换模板后
                    datasMap.put(name, sql);//将sql返回
                    return datasMap;
                }
            } catch (Exception e) {
                throw new RuntimeException(visuaSqlVo.getName() + ">>" + visuaSqlVo.getDesc() + ">>"
                        + visuaSqlVo.getValue() + "SQL模板替换有误>>" + e.getMessage());
            }
            // sql模板替换
            if (StringUtils.isEmpty(sql.trim())) {
                continue;
            }
            try {
                if (CommonConstants.VISUA_TYPE_CHECK.equals(visuaSqlVo.getType())) {
                    datasMap.put(ErrorCodeConstants.HASH_ERR, sql);
                    return datasMap;
                } else if (CommonConstants.VISUA_TYPE_SEL.equals(visuaSqlVo.getType())) {// 查
                    System.out.println("获取到的sql为"+sql);
                    List<Map<String, Object>> list = commonSqlService.getSqlQuery(sql, params,
                            visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), list);
                    realParamsMap.put(visuaSqlVo.getName(), list);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), list);
                    }
                } else if (CommonConstants.VISUA_TYPE_ADD.equals(visuaSqlVo.getType())) {// 增
                    System.out.println("sql模板为"+sql);
                    int addint = commonSqlService.addSql(sql, params, visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), addint);
                    realParamsMap.put(visuaSqlVo.getName(), addint);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), addint);
                    }
                } else if (CommonConstants.VISUA_TYPE_ADD_GET_ID.equals(visuaSqlVo.getType())) {// 增
                    System.out.println("sql模板为"+sql);
                    int addint = commonSqlService.addSqlAndGetId(sql, params, visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), addint);
                    realParamsMap.put(visuaSqlVo.getName(), addint);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), addint);
                    }
                } else if (CommonConstants.VISUA_TYPE_DEL.equals(visuaSqlVo.getType())) {// 删
                    int delint = commonSqlService.delSql(sql, params, visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), delint);
                    realParamsMap.put(visuaSqlVo.getName(), delint);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), delint);
                    }
                } else if (CommonConstants.VISUA_TYPE_EDIT.equals(visuaSqlVo.getType())) {// 改
                    System.out.println("sql模板为"+sql);
                    int upint = commonSqlService.updateSql(sql, params, visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), upint);
                    realParamsMap.put(visuaSqlVo.getName(), upint);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), upint);
                    }
                } else if (CommonConstants.VISUA_TYPE_ECHARTS.equals(visuaSqlVo.getType())) {// 图表
                    datasMap.put(visuaSqlVo.getName(), sql);
                    paramsMap.put(visuaSqlVo.getName(), sql);
                    realParamsMap.put(visuaSqlVo.getName(), sql);
                }else if (CommonConstants.VISUA_TYPE_REFLEX.equals(visuaSqlVo.getType())) {// 反射
                    ReflexClassDto reflexClassDto = jsonService.parseObject(sql, ReflexClassDto.class);
                    Map<String, Object>reflexParamsMap=new HashMap<>(); 
                    reflexParamsMap.putAll(realParamsMap);
                    if(reflexClassDto.getParams()!=null) {
                        Map<String, Object>map= jsonService.parseMap(reflexClassDto.getParams());// 参数Map
                        reflexParamsMap.putAll(map);
                    }
                    Class stuClass=Class.forName(reflexClassDto.getClassObj());
                    Object obj=SpringUtil.getBean(stuClass);
                    Method m =stuClass.getDeclaredMethod(reflexClassDto.getMethod(), Map.class);
                    Object retenObj=m.invoke(obj, reflexParamsMap);
                    System.out.println(retenObj);
                    paramsMap.put(visuaSqlVo.getName(), retenObj);
                    realParamsMap.put(visuaSqlVo.getName(), retenObj);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), retenObj);
                    }
                } else if (CommonConstants.VISUA_TYPE_MONGO_SEL.equals(visuaSqlVo.getType())) {//mong查询
                    //mongo的查询
                    MongoSqlDto mongoSqlDto = jsonService.parseObject(sql, MongoSqlDto.class);
                    List<Map> list=commonMongoService.find(mongoSqlDto);
                    paramsMap.put(visuaSqlVo.getName(), list);
                    realParamsMap.put(visuaSqlVo.getName(), list);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), list);
                    }
                }else if (CommonConstants.VISUA_TYPE_MONGO_DEL.equals(visuaSqlVo.getType())) {//mong删除
                    MongoSqlDto mongoSqlDto = jsonService.parseObject(sql, MongoSqlDto.class);
                    Map<String,Object>deleteResult=commonMongoService.remove(mongoSqlDto);
                    paramsMap.put(visuaSqlVo.getName(), deleteResult);
                    realParamsMap.put(visuaSqlVo.getName(), deleteResult);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), deleteResult);
                    }
                }else if (CommonConstants.VISUA_TYPE_MONGO_ADD.equals(visuaSqlVo.getType())) {//mong新增
                    // mongo的查询
                    MongoSqlDto mongoSqlDto = jsonService.parseObject(sql, MongoSqlDto.class);
                    commonMongoService.insert(mongoSqlDto);
                }else if (CommonConstants.VISUA_TYPE_ES_SEL.equals(visuaSqlVo.getType())){//ES查询
					List<Map<String, Object>> list = commonSqlService.getEsSqlQuery(sql);
					paramsMap.put(visuaSqlVo.getName(), list);
					realParamsMap.put(visuaSqlVo.getName(), list);
					if (visuaSqlVo.getIsReturn() == 1) {
						datasMap.put(visuaSqlVo.getName(), list);
						datasMap.put(CommonConstants.SUPPORT_TO_DO, visuaSqlExample.getSupportToDo());
						datasMap.put(CommonConstants.UDF_TO_DO, visuaSqlExample.getUdfToDo());
					}
				}else {// 查
                    List<Map<String, Object>> list = commonSqlService.getSqlQuery(sql, params,
                            visuaSqlVo.getDataSource());
                    paramsMap.put(visuaSqlVo.getName(), list);
                    realParamsMap.put(visuaSqlVo.getName(), list);
                    if (visuaSqlVo.getIsReturn() == 1) {
                        datasMap.put(visuaSqlVo.getName(), list);
                        datasMap.put(CommonConstants.SUPPORT_TO_DO, visuaSqlExample.getSupportToDo());
                        datasMap.put(CommonConstants.UDF_TO_DO, visuaSqlExample.getUdfToDo());

                    }
                }
                
                if(flag&&visuaSqlVo.getName().equals(name)
                        &&CommonConstants.TEST_MODE_TYPE_EXECSQL.equals(type)
                        ) {//测试
                    datasMap.put(visuaSqlVo.getName(), paramsMap.get(visuaSqlVo.getName()));
                    return datasMap;
                }
            } catch (Exception e) {
            	System.out.println("调用出错");
            	e.printStackTrace();
                throw new RuntimeException(visuaSqlVo.getName() + ">>" + visuaSqlVo.getDesc() + ">>"
                        + visuaSqlVo.getValue() + "SQL语句执行>>" + e.getMessage());
            }
        }
        return datasMap;
    }

}
