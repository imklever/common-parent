package com.isoftstone.common.networm.db;

import com.isoftstone.common.networm.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.util.JsonService;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CommonDB {
    @Autowired
    JsonService jsonService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 判断事保存还是更新
     * @param list：要保存或者插入的list<Map<>>>
     * @param cxdm：查询代码
     * @param bcdm：保存代码
     * @param gxdm：更新代码
     */
    public void IsInsertOrUpdateList(List<Map<String,Object>> list, String cxdm, String bcdm, String gxdm){
        for (Map<String,Object> m :list){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                if("[]".equals(map.get("datalist").toString())){
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }else{
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(gxdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                continue;
            }
        }
    }

    public void IsInsertAndUpdateList(List<Map<String,Object>> list, String cxdm, String bcdm, String gxdm,Map<String,Object> paraMap,String otherGxdm){
        for (Map<String,Object> m :list){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                if("[]".equals(map.get("datalist").toString())){
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }else{
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(gxdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }
                visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(otherGxdm);
                commBusinessRunService.getByParamBusinessCode(visuaSqlExample, jsonService.toJson(paraMap), null, null);
            } catch (Exception e) {
                logger.error(e.getMessage());
                continue;
            }
        }
    }

    public void IsInsertOrUpdateList(List<Map<String,Object>> list, String cxdm, String bcdm){
        for (Map<String,Object> m :list){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                if("[]".equals(map.get("datalist").toString())){
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                continue;
            }
        }
    }

    public void IsInsertAndUpdate(List<Map<String,Object>> list, String cxdm, String bcdm,String gxdm,Map<String,Object> paramMap){
        for (Map<String,Object> m :list){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                if("[]".equals(map.get("datalist").toString())){
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
                    commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
                }
                visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(gxdm);
                commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(paramMap), null, null);
            } catch (Exception e) {
                logger.error(e.getMessage());
                continue;
            }
        }
    }

    public void IsInsertOrUpdate(Map<String,Object> m, String cxdm, String bcdm){
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
        try {
            Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
            if("[]".equals(map.get("datalist").toString())){
                visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
                commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(m), null, null);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 查询新闻搜索关键字
     * @param cxdm 查询代码
     * @return 关键字列表
     */
    public List<Map<String,Object>> getSearchKeyWords(String cxdm){
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            Map<String, Object> mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, "{}", null, null);
            list = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));
            return list;
        }catch (Exception e){
            logger.error(e.getMessage());
            return list;
        }
    }

    public List<Map<String,Object>> selectMethod(String cxdm,Map<String,Object> paraMap){
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            Map<String, Object> mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, jsonService.toJson(paraMap), null, null);
            list = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));
            return list;
        }catch (Exception e){
            logger.error(e.getMessage());
            return list;
        }
    }

    public List<Map<String,Object>> getNetWormRules(Map<String,Object> paraMap,String cxdm){
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
            Map<String, Object> mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, jsonService.toJson(paraMap), null, null);
            list = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));;
            return list;
        }catch (Exception e){
            logger.error(e.getMessage());
            return list;
        }
    }

    public void updateMethond(Map<String,Object> paraMap,String gxdm){
        try {
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(gxdm);
            commBusinessRunService.getByParamBusinessCode(visuaSqlExample, jsonService.toJson(paraMap), null, null);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
    public void insertMethod(Map<String,Object> m,String bcdm){
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(bcdm);
        try {
            String json = jsonService.toJson(m);
            commBusinessRunService.getByParamBusinessCode(visuaSqlExample,json, null, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public int getResponseRawNum(String cxdm,Map<String,Object> paramMap){
        int num = 0;
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
        try {
            Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(paramMap), null, null);
            System.out.println(map.get("datalist").toString());
            String n = ((List<Map<String, Object>>) JSONArray.fromObject(map.get("datalist"))).get(0).get("num").toString();
            num = Integer.parseInt(n);
            return num;
        }catch (Exception e){
            logger.error(e.getMessage());
            return num;
        }
    }

    public boolean isExsit(Map<String,Object> paramMap,String cxdm){
        boolean flag = false;
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(cxdm);
        try {
            Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(paramMap), null, null);
            if("[]".equals(map.get("datalist").toString())){
                flag = true;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return flag;
        }
        return flag;
    }
}
