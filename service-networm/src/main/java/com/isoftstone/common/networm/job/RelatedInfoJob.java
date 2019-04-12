package com.isoftstone.common.networm.job;


import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.networm.parse.RelatedInfoParse;
import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.util.JsonService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.isoftstone.common.networm.util.Constant.XlRelatedInfoUrl;
import static com.isoftstone.common.networm.util.Constant.ofWeekRelatedInfoUrl;

@Component
@Repository
public class RelatedInfoJob {
    @Autowired
    UsedMethod usedMethod;
    @Autowired
    JsonService jsonService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;
    @Autowired
    CommonDB cdb ;

    static RelatedInfoParse rip = new RelatedInfoParse();

    /**
     * 新浪相关资讯
     */
    public void SinaRelatedInfoJob(){
        try {
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("S12014");
            Map<String, Object> mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, "{}", null, null);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));
            for(Map<String,Object> m :dataList){
                int professionalKeyWordId = Integer.parseInt(m.get("pid").toString());
                String type = m.get("type").toString();
                String searchStr = m.get("professional_keyword").toString();
                int enterpriseKeyWordId = 0;
                if ("处理器".equals(type) || "固件".equals(type) || "操作系统".equals(type)){
                    enterpriseKeyWordId = Integer.parseInt(m.get("eid").toString());
                    searchStr = searchStr + " + " + m.get("enterprise_keyword");
                }
                int num = rip.sinaContentNumParse(usedMethod.getQuesMethod(XlRelatedInfoUrl(searchStr)));
                double page = Math.ceil(num/20.0);
                for (int i=0;i<page;i++){
                    Thread.sleep(usedMethod.threadSleepTime(20000));
                    List<String> urlList = rip.sinaContentInfoUrlParse(usedMethod.getQuesMethod(XlRelatedInfoUrl(searchStr) + "&page="+ (i+1)));
                    for (String url : urlList) {
                        List<Map<String, Object>> list = new ArrayList<>();
                        Map<String, Object> map = rip.sinaContentInfoParse(url,usedMethod.getQuesMethod(url));
                        if (!map.isEmpty()){
                            map.put("professional_keyword_id",professionalKeyWordId);
                            map.put("enterprise_keyword_id",enterpriseKeyWordId);
                            list.add(map);
                            cdb.IsInsertOrUpdateList(list,"S12012","S12013");
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void ofWeekRelatedInfoJob(){
        try{
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("S12014");
            Map<String, Object> mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, "{}", null, null);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));
            for(Map<String,Object> m :dataList) {
                int professionalKeyWordId = Integer.parseInt(m.get("pid").toString());
                String type = m.get("type").toString();
                String searchStr = m.get("professional_keyword").toString();
                int enterpriseKeyWordId = 0;
                if ("处理器".equals(type) || "固件".equals(type) || "操作系统".equals(type)) {
                    enterpriseKeyWordId = Integer.parseInt(m.get("eid").toString());
                    searchStr = searchStr + " + " + m.get("enterprise_keyword");
                }
                String url = ofWeekRelatedInfoUrl(searchStr);
                int pageNum = rip.ofWeekContentNumParse(usedMethod.getQuesMethod(url));
                for (int i = 1; i <= pageNum; i++) {
                    Thread.sleep(usedMethod.threadSleepTime(20000));
                    List<String> urlList = rip.ofWeekContentInfoUrlParse(usedMethod.getQuesMethod(url + "&pagenum=" + i));
                    for (String oneUrl : urlList) {
                        List<Map<String, Object>> list = new ArrayList<>();
                        Map<String,Object> map = rip.ofWeekContentInfoParse(oneUrl, usedMethod.getQuesMethod(oneUrl));
                        if (!map.isEmpty()){
                            map.put("professional_keyword_id",professionalKeyWordId);
                            map.put("enterprise_keyword_id",enterpriseKeyWordId);
                            list.add(map);
                            cdb.IsInsertOrUpdateList(list,"S12012","S12013");
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

//    public void sinaContentInfoRuleJob(List<Map<String,Object>> rules,List<Map<String,Object>> urls){
//        for(Map<String,Object> urlInfo: urls){
//            String id = urlInfo.get("id").toString();
//            String source = urlInfo.get("source").toString();
//            int pid = Integer.parseInt(urlInfo.get("pid").toString());
//            int eid = Integer.parseInt(urlInfo.get("eid").toString());
//            String detialInfo = urlInfo.get("detial_info").toString();
//            Map<String,Object> urlMap = jsonService.parseMap(detialInfo);
//        }
//
//    }

    public static void main(String[] args) {
//        System.out.println(rip.sinaContentInfoParse(
//                "http://games.sina.com.cn/y/n/2014-06-05/1122789171.shtml",
//                getQuesMethod("http://games.sina.com.cn/y/n/2014-06-05/1122789171.shtml")));
//        String url = "https://ee.ofweek.com/2018-06/ART-8420-2801-30241516.html";
//        System.out.println(rip.ofWeekContentInfoParse(
//                url,
//                getQuesMethod(url)));
     }

}
