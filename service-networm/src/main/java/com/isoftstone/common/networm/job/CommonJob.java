package com.isoftstone.common.networm.job;

import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.parse.CommonParse;
import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.util.JsonService;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CommonJob {
    @Autowired
    UsedMethod usedMethod;
    @Autowired
    JsonService jsonService;
    @Autowired
    private CommonDB commonDB;
    @Autowired
    CommonParse commonParse;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String codes  = "";

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public void saveListRequestUrlJob(Map<String,Object> parseParam,String targetTable){
        String [] code = usedMethod.getModuleCodes("saveRequestUrl",codes);
        if(code != null){
            int type = Integer.parseInt(parseParam.get("type").toString());
            int page = Integer.parseInt(parseParam.get("page").toString());
            for(int i=1;i<=page;i++){
                try{
                    String url = parseParam.get("requestUrl") + "&"+ parseParam.get("pageFiled") + "=" + i;
                    parseParam.put("url",url);
                    parseParam.put("targetTable",targetTable);
                    if(commonDB.isExsit(parseParam,code[0])){
                        Map<String,Object> respMap = new HashMap<>();
                        respMap.put("targetTable",targetTable);
                        respMap.put("source",parseParam.get("source").toString());
                        respMap.put("type",type);
                        respMap.put("url",url);
                        respMap.put("pid",parseParam.get("pid") == null ? 0 : parseParam.get("pid").toString());
                        respMap.put("eid",parseParam.get("eid") == null ? 0 : parseParam.get("eid").toString());
                        commonDB.insertMethod(respMap,code[1]);
                    }
                }catch (Exception e){
                    logger.error("请求新闻列表error:"+e.getMessage());
                }
            }
        }
    }

    public void parseInfoJob(List<Map<String,Object>> respList,String targetTable){
        for(Map<String,Object> respMap : respList){
            try{
                int type = Integer.parseInt(respMap.get("type").toString());
                String source = respMap.get("source").toString();
                int pid = Integer.parseInt(respMap.get("pid").toString());
                int eid = Integer.parseInt(respMap.get("eid").toString());
                String resp = respMap.get("respond").toString();
                List<Map<String,Object>> ruleList = new ArrayList();
                if(type == 0){
                    String [] code = usedMethod.getModuleCodes("listParse",codes);
                    if(code != null){
                        ruleList = commonDB.getNetWormRules(respMap,code[0]);
                        for(Map<String,Object> ruleMap : ruleList){
                            Map<String,Object> parseParam = new HashMap<>();
                            parseParam.put("source", source);
                            parseParam.put("pid", pid);
                            parseParam.put("eid", eid);
                            parseParam.put("rule",ruleMap.get("rules").toString());
                            parseParam.put("isApi",ruleMap.get("is_api").toString());
                            parseParam.put("targetTable",targetTable);
                            List<Map<String,Object>> list = commonParse.newsListParse(parseParam,resp);
                            commonDB.IsInsertOrUpdateList(list,code[1],code[2]);
                        }
                        String requestTable = usedMethod.getTargetTable(respMap,code[3],"requestTable");
                        respMap.put("targetTable",requestTable);
                        if(!commonDB.isExsit(respMap,code[4])){
                            commonDB.updateMethond(respMap,code[5]);
                        }
                    }
                }else{
                    String [] code = usedMethod.getModuleCodes("detialParse",codes);
                    commonParse.setCode(codes);
                    if(code != null){
                        ruleList = commonDB.getNetWormRules(respMap,code[0]);
                        List<Map<String,Object>> list = new ArrayList<>();
                        Set keySet = null;
                        for(Map<String,Object> ruleMap : ruleList){
                            Map<String,Object> parseParam = new HashMap<>();
                            parseParam.put("source", source);
                            parseParam.put("pid", pid);
                            parseParam.put("eid", eid);
                            parseParam.put("rule",ruleMap.get("rules").toString());
                            parseParam.put("url",respMap.get("url").toString());
                            parseParam.put("isApi",ruleMap.get("is_api").toString());
                            parseParam.put("targetTable",targetTable);
                            Map<String,Object> map = commonParse.detialInfoParse(parseParam,resp);
                            keySet = map.keySet();
                            list.add(map);
                        }
                        if(list.size() > 1){
                            Map<String,Object> map = new HashMap<>();
                            for(int i=0;i<list.size();i++){
                                for(int j = list.size()-1;j>i;j--){
                                    for(Object key : keySet){
                                        String lengthI = list.get(i).get(key.toString()).toString();
                                        String lengthJ = list.get(j).get(key.toString()).toString();
                                        String str = "";
                                        if(lengthI.length() > lengthJ.length()){
                                            str = lengthI;
                                        }else{
                                            str = lengthJ;
                                        }
                                        Object keyStr = map.get(key.toString());
                                        if (keyStr == null){
                                            map.put(key.toString(),lengthI);
                                        }else if(keyStr.toString().length() < str.length()){
                                            map.put(key.toString(),str);
                                        }
                                    }
                                }
                            }
                            list.clear();
                            list.add(map);
                        }
                        for (Map<String,Object> m :list){
                            if(commonDB.isExsit(m,code[1])){
                                commonDB.insertMethod(m,code[2]);
                                commonDB.insertMethod(m,code[7]);
                            }else{
                                commonDB.updateMethond(m,code[3]);
                            }
                        }
                        String requestTable = usedMethod.getTargetTable(respMap,code[4],"requestTable");
                        respMap.put("targetTable",requestTable);
                        for(Map m :list){
                            if(!commonDB.isExsit(m,code[5])){
                                commonDB.updateMethond(respMap,code[6]);
                            }
                        }
                    }
                }
            }catch(Exception e){
                logger.error("解析Error"+ e.getMessage());
                continue;
            }
        }
    }

    /**
     * 保存请求响应表
     * @param parseParam 规则
     */
    public void responseListJob(Map<String,Object> parseParam){
        String [] code = usedMethod.getModuleCodes("listPost",codes);
        usedMethod.setCode(codes);
        if(code != null){
            int type = Integer.parseInt(parseParam.get("type").toString());
            try{
                String url = parseParam.get("url").toString();
                if(commonDB.isExsit(parseParam,code[0])){
                    Document resopnd = usedMethod.getQuesMethod(url);
                    if (resopnd != null){
                        Map<String,Object> respMap = new HashMap<>();
                        respMap.put("source",parseParam.get("source").toString());
                        respMap.put("type",type);
                        respMap.put("url",url);
                        respMap.put("respond",resopnd.toString());
                        respMap.put("pid",parseParam.get("pid") == null ? 0 : parseParam.get("pid").toString());
                        respMap.put("eid",parseParam.get("eid") == null ? 0 : parseParam.get("eid").toString());
                        commonDB.insertMethod(respMap,code[1]);
                    }
                }
            }catch (Exception e){
                    logger.error("请求新闻列表error:"+e.getMessage());
            }
        }
    }

    public void responseDetialInfoJob(List<Map<String,Object>> newsList){
        String [] code = usedMethod.getModuleCodes("detialPost",codes);
        usedMethod.setCode(codes);
        if(code != null){
            for(Map<String,Object> news : newsList){
                try{
                    Map<String,Object> map = new HashMap<>();
//                    Map<String,Object> urlMap = jsonService.parseMap(news.get("detial_info").toString());
                    String url = news.get("url").toString();
                    map.put("source",news.get("source").toString());
                    map.put("type",Integer.parseInt(news.get("type").toString()));
                    map.put("url",url);
                    map.put("pid",Integer.parseInt(news.get("pid").toString()));
                    map.put("eid",Integer.parseInt(news.get("eid").toString()));
                    if(commonDB.isExsit(map,code[0])){
                        Document resopnd = usedMethod.getQuesMethod(url);
                        if (resopnd != null){
                            map.put("respond",resopnd.toString());
                            commonDB.insertMethod(map,code[1]);
                        }
                    }
                }catch (Exception e){
                    logger.error("请求文章详情error;"+e.getMessage());
                    continue;
                }

            }
        }
    }

    public void asdf(){
        String url = "https://item.jd.com/10722898427.html";
        Map<String,Object> parseParam = new HashMap<>();
        parseParam.put("source", "JD");
        parseParam.put("pid", 59);
        parseParam.put("eid", 0);
        parseParam.put("rule","{\"newscode\":{\"select\":\"div.left-btns a\",\"isText\":0,\"attrFiled\":\"data-id\"},\"title\":{\"select\":\"div.sku-name\",\"isText\":1},\"keyword\":{\"select\":\"meta[name=keywords]\",\"isText\":0,\"attrFiled\":\"content\"},\"price\":{\"select\":\"div.dd > span.p-price > span.price\",\"isText\":1},\"shopUrl\":{\"select\":\"div.mt h3 a\",\"isText\":0,\"attrFiled\":\"href\"},\"shop\":{\"select\":\"div.mt h3 a\",\"isText\":1},\"detialParam\":{\"select\":\"div.p-parameter ul\",\"isText\":1}}");
        parseParam.put("url",url);
        parseParam.put("isApi",0);
        parseParam.put("targetTable","ai_networm_infodetial");
        usedMethod.setCode("S12039");
        try {
            Map<String,Object> list = commonParse.detialInfoParse(parseParam,usedMethod.getQuesMethod(url).toString());
            System.out.println(list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
