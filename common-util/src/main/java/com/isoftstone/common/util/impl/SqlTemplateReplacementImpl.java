package com.isoftstone.common.util.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.plugins.visua.vo.VisuaSqlInputDataVo;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.SqlTemplateReplacement;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
@Service
public class SqlTemplateReplacementImpl implements SqlTemplateReplacement{
	@Autowired
	JsonService jsonService;
	static String IS_SKIP="isSkip";
	static String NAME="name";
	static String SYSTEM="system";
	@Override
	public String templateReplacement(Map<String, Object> paramsMap, String sqlTemplate) throws Exception {
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_0);  
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("option",sqlTemplate);
		configuration.setTemplateLoader(stringLoader);
	    configuration.setDefaultEncoding("UTF-8");
	    Template template;
		template = configuration.getTemplate("option","utf-8");
		Writer writer  =  new StringWriter();
	    template.process(paramsMap, writer);
	    return writer.toString().replaceAll("\n", " ").replaceAll("\\s"," ").replaceAll("\r"," ");
	}
	@Override
	public Map<String, Object> createReplaceMap(Map<String, Object> allMap, String inputJson, String params) {
		if(!StringUtils.isEmpty(params)){
			Map<String, Object> maparams=jsonService.parseMap(params);//前台参数
			JSONArray jsonArray=JSONObject.parseArray(inputJson);
			JSONObject isSkipMap=new JSONObject();
			for (Object object : jsonArray) {
				JSONObject jsonObject=(JSONObject) object;
				if(jsonObject.containsKey(IS_SKIP)&&jsonObject.getBooleanValue(IS_SKIP)) {
					isSkipMap.put(jsonObject.getString(NAME), true);
				}
			}
			isSkipMap.put(SYSTEM, true);
			for (String key : maparams.keySet()) {
				if(maparams.get(key) instanceof List)
				{
					try {
						allMap.put(key,jsonService.parseArray(maparams.get(key).toString(), Object.class));
					} catch (Exception e) {
						allMap.put(key,maparams.get(key));
					}
				}
				else {
					if(isSkipMap.containsKey(key)) {
						try {
							allMap.put(key,jsonService.parseArray(maparams.get(key).toString(), Object.class));
						} catch (Exception e) {
							allMap.put(key,maparams.get(key));
						}
					}else {
						allMap.put(key, "#{map."+key+"}");
					}
				}
			}
		}
		return allMap;
	}
	@Override
	public Map<String, Object> createReplaceTestMap(Map<String, Object> paramsMap, String inputJson) {
		if(!StringUtils.isEmpty(inputJson)) {
			List<VisuaSqlInputDataVo> visuaSqlInputDataVos=
					jsonService.parseArray(inputJson, VisuaSqlInputDataVo.class);
			for (VisuaSqlInputDataVo visuaSqlInputDataVo : visuaSqlInputDataVos) {
				if(visuaSqlInputDataVo.isSkip()) {
					try {
						paramsMap.put(visuaSqlInputDataVo.getName(),
								JSONObject.parseArray(visuaSqlInputDataVo.getTestVal()));
					} catch (Exception e) {
						paramsMap.put(visuaSqlInputDataVo.getName(),
								visuaSqlInputDataVo.getTestVal());
					}
				}else {
					paramsMap.put(visuaSqlInputDataVo.getName(), 
							"#{map."+visuaSqlInputDataVo.getName()+"}"
							);
				}
			}
		}
		return paramsMap;
	}
	@Override
	public String createReplaceTestStr(String inputJson) {
		Map<String,Object>paramsMap=new HashMap<>();
		if(!StringUtils.isEmpty(inputJson)) {
			
			List<VisuaSqlInputDataVo> visuaSqlInputDataVos=
					jsonService.parseArray(inputJson, VisuaSqlInputDataVo.class);
			for (VisuaSqlInputDataVo visuaSqlInputDataVo : visuaSqlInputDataVos) {
					try {
						paramsMap.put(visuaSqlInputDataVo.getName(),
								JSONObject.parseArray(visuaSqlInputDataVo.getTestVal()));
					} catch (Exception e) {
						paramsMap.put(visuaSqlInputDataVo.getName(),
								visuaSqlInputDataVo.getTestVal());
					}
			}
		}
		return jsonService.toJson(paramsMap);
		
	}
    @Override
    public Map<String, Object> createReplaceMap(int versionNo, Map<String, Object> allMap, String inputJson,
            String params, boolean type) {
        if(versionNo==1) {
            return createReplaceMap(allMap,inputJson,params); 
        }else if(versionNo==2){
            Map<String, Object> maparams=jsonService.parseMap(params);//前台参数
            Map<String,VisuaSqlInputDataVo>map=new HashMap<String, VisuaSqlInputDataVo>();
            List<VisuaSqlInputDataVo> visuaSqlInputDataVos= jsonService.parseArray(inputJson, VisuaSqlInputDataVo.class);
            for (VisuaSqlInputDataVo visuaSqlInputDataVo : visuaSqlInputDataVos) {
                map.put(visuaSqlInputDataVo.getName(), visuaSqlInputDataVo);
            }
            VisuaSqlInputDataVo visuaSqlInputDataVo=null;
            for (String  key : maparams.keySet()) {
                if(key.equals(SYSTEM)) {continue;}
                else if(map.containsKey(key)) {
                    visuaSqlInputDataVo=map.get(key);
                    if(visuaSqlInputDataVo.isSkip()) {
                        allMap.put(key,getMaparams(maparams,key,visuaSqlInputDataVo.getDataType())
                                );//跳过注入时要验证各种参数!
                    }else {
                        allMap.put(key, "#{map."+key+"}");
                    }
                }else {
                    allMap.put(key, "#{map."+key+"}");
                }
            }
        }
        return allMap;
    }
    private Object getMaparams(Map<String, Object> maparams,String key,String dateType) {
        Object value=maparams.get(key);
        Object v=null;
        switch (dateType) {//类型
        case "list":
            if(value instanceof List) {
                v=value;
            }else if(value  instanceof String) {
                v=JSONObject.parseArray((String)value);
            }else {
                throw new RuntimeException("list对应的数据类型错误！");
            }
            break;
        case "listMap"://list中包含map
            if(value instanceof List) {
                v=value;
            }else if(value  instanceof String) {
                v=JSONObject.parseArray((String)value);
            }else {
                throw new RuntimeException("list对应的数据类型错误！");
            }
            break;
        case "string":
            if(value instanceof List) {
                v=jsonService.toJson(value);
            }else if(value  instanceof String) {
                v=value;
            }else if(value  instanceof Map) {
                v=jsonService.toJson(value);
            }else {
                throw new RuntimeException("string对应的数据类型错误！");
            }
            if(sql_inj((String)v)) {
                throw new RuntimeException(key+"--非法参数！");    
            }
            break;
        default:
            if(value instanceof String) {
                v=value;
                if(sql_inj((String)v)) {
                    throw new RuntimeException(key+"--非法参数！");    
                }
            }else if(value instanceof Boolean) {
                v=value;
            }else if(value instanceof Integer) {
                v=value;
            }else{
                throw new RuntimeException("参数类型还未定制！请联系管理员！");
            }
            break;
        }
        return v;
    }

    public static boolean sql_inj(String str) {
        String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
        String inj_stra[] = inj_str.split("\\|");
        for (int i = 0; i >= inj_stra.length; i++) {
            if (str.indexOf(inj_stra[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

}
