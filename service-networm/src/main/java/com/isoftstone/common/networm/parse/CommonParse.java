package com.isoftstone.common.networm.parse;

import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.util.JsonService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.*;

@Component
@Repository
public class CommonParse {
    @Autowired
    UsedMethod usedMethod;
    @Autowired
    JsonService jsonService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String code = "";

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String,Object> detialInfoParse(Map<String,Object> parseParam, String resp) throws Exception{
        Map<String,Object> map = new HashMap<>();
        usedMethod.setCode(code);
        if(resp != null){
            Document doc = Jsoup.parse(resp);
            Map<String,Map<String,Object>> ruleMap = (Map<String,Map<String,Object>>) net.sf.json.JSONObject.fromObject(parseParam.get("rule").toString());
            int isApi = Integer.parseInt(parseParam.get("isApi").toString());
            String url = parseParam.get("url").toString();
            if(isApi == 1){
                String regex = ruleMap.get("all").get("isRegex").toString();
                String infoStr = usedMethod.getRegexAfterStr(regex,resp);
                if(!"".equals(infoStr)){
                    String allStr = ruleMap.get("all").get("select").toString();
                    Map m = net.sf.json.JSONObject.fromObject(infoStr);
                    infoStr = parseMapDG(allStr.split(","),0,m);
                    m = net.sf.json.JSONObject.fromObject(infoStr);
                    ruleMap.remove("all");
                    Set keys = ruleMap.keySet();
                    for(Object obj :keys){
                        String key = obj.toString();
                        Map<String,Object> keyMap = ruleMap.get(key);
                        map.put(key,m.get(keyMap.get("select").toString()));
                    }
                }
            }else {
                Set keys = ruleMap.keySet();
                for(Object key1 : keys){
                    String key = key1.toString();
                    Map<String,Object> keyMap = ruleMap.get(key);
                    String str = "";
                    if(keyMap.get("isPaging") == null){
                        if(keyMap.get("isRegex") == null){
                            if(Integer.parseInt(keyMap.get("isText").toString()) == 0){
                                str += doc.select(keyMap.get("select").toString()).attr(keyMap.get("attrFiled").toString());
                            }else if(Integer.parseInt(keyMap.get("isText").toString()) == 1){
                                if(keyMap.get("isSelfText") != null){
                                    str += doc.select(keyMap.get("select").toString()).first().ownText();
                                }else {
                                    str += doc.select(keyMap.get("select").toString()).text();
                                }
                            }
                        }else{
                            str += usedMethod.getRegexAfterStr(keyMap.get("select").toString(),doc.toString());
                        }
                    }else {
                        Elements pageEle = doc.select(keyMap.get("pageSelect").toString());
                        str += doc.select(keyMap.get("select").toString()).text();
                        for(int i=1;i<pageEle.size()-1;i++){
                            String fetchUrl = usedMethod.getRegexAfterStr("(.*)/.*\\.*html",url) + "/" + pageEle.get(i).attr("href");
                            Document d = usedMethod.getQuesMethod(fetchUrl);
                            str += d == null ? "": d.select(keyMap.get("select").toString()).text();
                        }
                    }
                    map.put(key,str);
                }
            }
            for(Object a :map.keySet()){
                String aa = a.toString();
                if (aa.toUpperCase().indexOf("URL") > 0){
                    String aaa = map.get(aa).toString();
                    if(!aaa.startsWith("http") && aaa.startsWith("//")){
                        aaa = "http:" + aaa;
                        map.put(aa,aaa);
                    }
                }
            }
            map.put("url",url);
            int id = map.hashCode();
            String detialInfo =  jsonService.toJson(map);
            map.clear();
            map.put("id",id);
            map.put("detial_info",detialInfo);
            map.put("source",parseParam.get("source").toString());
            map.put("targetTable",parseParam.get("targetTable"));
            map.put("pid",Integer.parseInt(parseParam.get("pid").toString()));
            map.put("eid",Integer.parseInt(parseParam.get("eid").toString()));
        }
        return map;
    }

    public List<Map<String,Object>> newsListParse(Map<String,Object> parseParam,String resp) throws Exception{
        List<Map<String,Object>> list = new ArrayList<>();
        int isApi = Integer.parseInt(parseParam.get("isApi").toString());
        Map<String,Map<String,Object>> ruleMap = (Map<String,Map<String,Object>>) net.sf.json.JSONObject.fromObject(parseParam.get("rule").toString());
        if (resp != null){
            Document doc = Jsoup.parse(resp);
            if(isApi == 0){
                Elements ele = doc.select(ruleMap.get("all").get("select").toString());
                if(ruleMap.get("all").get("isCommon") == null){
                    ruleMap.remove("all");
                    Set keys = ruleMap.keySet();
                    for(Element e : ele){
                        Map<String,Object> map = new HashMap<>();
                        try{
                            for(Object key1 : keys) {
                                String key = key1.toString();
                                Map<String,Object> keyMap = ruleMap.get(key);
                                String str = "";
                                if(keyMap.get("isMerge") != null){
                                    str += keyMap.get("isMerge").toString();
                                }
                                if(Integer.parseInt(keyMap.get("isText").toString()) == 0){
                                    String lianjieurl = e.select(keyMap.get("select").toString()).attr(keyMap.get("attrFiled").toString());
                                    if(lianjieurl.startsWith("./")){
                                        str += lianjieurl.replace("./","/");
                                    }else{
                                        str += lianjieurl;
                                    }
                                }else if(Integer.parseInt(keyMap.get("isText").toString()) == 1){
                                    if(keyMap.get("isSelfText") != null){
                                        str += e.select(keyMap.get("select").toString()).first().ownText();
                                    }else {
                                        str += e.select(keyMap.get("select").toString()).text();
                                    }
                                }
                                map.put(key,str);
                            }
//                            int id = map.hashCode();
//                            String detialInfo =  jsonService.toJson(map);
//                            map.put("id",id);
//                            map.put("detial_infoinfo",detialInfo);
                            String url = map.get("url").toString();
                            if(!url.startsWith("http") && url.startsWith("//")){
                                url = "http:" + url;
                            }
                            map.clear();
                            map.put("source",parseParam.get("source").toString());
                            map.put("targetTable",parseParam.get("targetTable"));
                            map.put("url",url);
                            map.put("type",1);
                            map.put("pid",Integer.parseInt(parseParam.get("pid").toString()));
                            map.put("eid",Integer.parseInt(parseParam.get("eid").toString()));
                            list.add(map);
                        }catch (Exception eeee){
                            continue;
                        }
                    }
                }else{
                    int count = Integer.parseInt(ruleMap.get("all").get("groupNum").toString());
                    ruleMap.remove("all");
                    Set keys = ruleMap.keySet();
                    List<Element> abc = usedMethod.fixedGrouping(ele,count);
                        for(Element e : abc){
                            Map m = new HashMap();
                            try{
                                for(Object key1 : keys) {
                                    String key = key1.toString();
                                    Map<String,Object> keyMap = ruleMap.get(key);
                                    String str = "";
                                    if(keyMap.get("isMerge") != null){
                                        str += keyMap.get("isMerge").toString();
                                    }
                                    if(Integer.parseInt(keyMap.get("isText").toString()) == 0){
                                        str += e.select(keyMap.get("select").toString()).attr(keyMap.get("attrFiled").toString());
                                    }else if(Integer.parseInt(keyMap.get("isText").toString()) == 1){
                                        if(keyMap.get("isSelfText") != null){
                                            str += e.select(keyMap.get("select").toString()).first().ownText();
                                        }else {
                                            str += e.select(keyMap.get("select").toString()).text();
                                        }
                                    }
                                    m.put(key,str);
                                }
                                String url = m.get("url").toString();
                                if(!url.startsWith("http") && url.startsWith("//")){
                                    url = "http:" + url;
                                }
                                m.clear();
                                m.put("source",parseParam.get("source").toString());
                                m.put("targetTable",parseParam.get("targetTable"));
                                m.put("url",url);
                                m.put("type",1);
                                m.put("pid",Integer.parseInt(parseParam.get("pid").toString()));
                                m.put("eid",Integer.parseInt(parseParam.get("eid").toString()));
                                list.add(m);
                            }catch (Exception eeee){
                                continue;
                            }
                        }
                    }
            }else{
                String allStr = ruleMap.get("all").get("select").toString();
                Map map = net.sf.json.JSONObject.fromObject(doc.body().text());
                String infoStr = parseMapDG(allStr.split(","),0,map);
                List infoList = net.sf.json.JSONArray.fromObject(infoStr);
                ruleMap.remove("all");
                Set keySet = ruleMap.keySet();
                for(Object obj :infoList){
                    Map<String,Object> infoMap = new HashMap<>();
                    map = jsonService.parseMap(obj.toString());
                    for(Object keyStr : keySet){
                        String key = keyStr.toString();
                        Map keyInfoMap = ruleMap.get(key);
                        String str = "";
                        if(keyInfoMap.get("isReplace") != null){
                            str = keyInfoMap.get("isReplace").toString().replace("**",map.get(keyInfoMap.get("select").toString()).toString());
                        }else {
                            str = map.get(keyInfoMap.get("select").toString()).toString();
                        }
                        infoMap.put(key,str);
                    }
//                    int id = infoMap.hashCode();
//                    String detialInfo =  jsonService.toJson(infoMap);
//                    infoMap.clear();
//                    infoMap.put("id",id);
//                    infoMap.put("source",parseParam.get("source").toString());
//                    infoMap.put("detial_info",detialInfo);
//                    infoMap.put("targetTable",parseParam.get("targetTable"));
//                    infoMap.put("pid",Integer.parseInt(parseParam.get("pid").toString()));
//                    infoMap.put("eid",Integer.parseInt(parseParam.get("eid").toString()));
                    String url = infoMap.get("url").toString();
                    if(!url.startsWith("http") && url.startsWith("//")){
                        url = "http:" + url;
                    }
                    infoMap.clear();
                    infoMap.put("source",parseParam.get("source").toString());
                    infoMap.put("targetTable",parseParam.get("targetTable"));
                    infoMap.put("url",url);
                    infoMap.put("type",1);
                    infoMap.put("pid",Integer.parseInt(parseParam.get("pid").toString()));
                    infoMap.put("eid",Integer.parseInt(parseParam.get("eid").toString()));
                    list.add(infoMap);
                }
            }
        }
        return list;
    }

    private String parseMapDG(String [] a,int count,Map map){
        String aaa = "";
        if(a.length == 1){
            return map.get(a[0]).toString();
        }else if (count <= a.length-1 ){
            try{
                Map b = (Map) map.get(a[count]);
                count ++;
                aaa = this.parseMapDG(a,count,b);
            }catch (Exception e){
                return map.get(a[count]).toString();
            }
        }
        return aaa;
    }

}
