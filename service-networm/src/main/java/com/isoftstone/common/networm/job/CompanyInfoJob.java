package com.isoftstone.common.networm.job;

import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.networm.parse.CompanyInfoParse;
import com.isoftstone.common.networm.util.UsedMethod;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.util.JsonService;
import net.sf.json.JSONArray;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.List;
import static com.isoftstone.common.networm.util.Constant.*;

@Component
@Repository
public class CompanyInfoJob {

    @Autowired
    UsedMethod usedMethod;
    @Autowired
    JsonService jsonService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;
    @Autowired
    CompanyInfoParse ci;
    @Autowired
    CommonDB cdb ;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private String code = "";

    public void setCode(String code) {
        this.code = code;
    }

    public void GradeTypeJob(){
        Document respList = usedMethod.getQuesMethod(usedMethod.getQuesUrl("GradeTypeListUrl",new ArrayList<>()));
        List<Map<String,Object>> hylist = ci.GradeTypeParse(respList);
        if(hylist.size() > 0){
            cdb.IsInsertOrUpdateList(hylist,"S12001","S12002","S12003");
        }
    }


    public void CompanyInfoList(){
        String [] busiCode = usedMethod.getModuleCodes("companyInfoFetch",code);
        if(busiCode != null){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(busiCode[0]);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,"{}", null, null);
                List<Map<String,String>> dataList = (List<Map<String,String>>) JSONArray.fromObject(map.get("datalist"));
                for (Map<String,String> m : dataList){
                    List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                    String hy = m.get("GRADE_TYPE");
                    List<String> paramList = new ArrayList<>();
                    paramList.add(hy);
                    String countUrl = usedMethod.getQuesUrl("CompanyCountUrl",paramList);
                    usedMethod.setCode(code);
                    Document countResp = usedMethod.getQuesMethod(countUrl);
                    if(countResp != null){
                        int count = ci.getInfoListCountParse(countResp);
                        for (int i=1;i<=Math.ceil(count/100.0);i++){
                            paramList.clear();
                            paramList.add(i+"");
                            paramList.add(hy);
                            String listUrl = usedMethod.getQuesUrl("CompanyListUrl",paramList);
                            Document infoResp = usedMethod.getQuesMethod(listUrl);
                            if (infoResp != null){
                                for(String info : ci.CompanyInfoListParse(infoResp)){
                                    Map<String,Object> company = new HashMap<String, Object>();
                                    try {
                                        Thread.sleep(usedMethod.threadSleepTime(10000));
                                        String code = info.split(",")[1];
                                        paramList.clear();
                                        paramList.add(code);
                                        String detialUrl = usedMethod.getQuesUrl("CompanyDetailUrl",paramList);
                                        company = ci.CommanyDetialInfoParse(hy,info,usedMethod.getQuesMethod(detialUrl));
                                    } catch (Exception e){
                                        continue;
                                    }
                                    list.add(company);
                                }
                            }
                        }
                    }
                    cdb.IsInsertOrUpdateList(list,busiCode[1],busiCode[2],busiCode[3]);
                }
            } catch (Exception e) {
                logger.error("公司基本信息："+e.getMessage());
            }
        }
    }

    public void companyFinanceInfoJob(){
        String [] busiCode = usedMethod.getModuleCodes("companyFinanceJob",code);
        if(busiCode != null){
            VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(busiCode[0]);
            try {
                Map<String, Object> map = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, "{}", null, null);
                List<Map<String, String>> dataList = (List<Map<String, String>>) JSONArray.fromObject(map.get("datalist"));
                for (Map<String, String> m : dataList) {
                    Map<String,Object> paramMap = new HashMap<>();
                    visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode(busiCode[1]);
                    paramMap.put("GradeType",m.get("GRADE_TYPE"));
                    Map<String, Object> map1 = commBusinessRunService.getByParamBusinessCode(visuaSqlExample,jsonService.toJson(paramMap), null, null);
                    List<Map<String, String>> dataList1 = (List<Map<String, String>>) JSONArray.fromObject(map1.get("datalist"));
                    usedMethod.setCode(code);
                    for(Map<String,String> mm : dataList1){
                        String code = mm.get("COMPCODE");
                        List<String> paramList = new ArrayList<>();
                        paramList.add(code);
                        try {
                            List<Map<String,Object>> list1 = ci.financeInfoParse(usedMethod.getQuesMethod(usedMethod.getQuesUrl("FinanceLRBUrl",paramList)),code,0);
                            List<Map<String,Object>> list2 = ci.financeInfoParse(usedMethod.getQuesMethod(usedMethod.getQuesUrl("FinanceZCFZBUrl",paramList)),code,1);
                            List<Map<String,Object>> list3 = ci.financeInfoParse(usedMethod.getQuesMethod(usedMethod.getQuesUrl("FinanceYYBBUrl",paramList)),code,2);
                            List<Map<String,Object>> list4 = usedMethod.mergeListMap(list1,list2);
                            List<Map<String,Object>> list5 = usedMethod.mergeListMap(list4,list3);
                            cdb.IsInsertOrUpdateList(list5,busiCode[2],busiCode[3],busiCode[4]);
                        }catch (Exception e){
                            continue;
                        }
                    }
                }
            } catch (Exception e){
                logger.error("公司财务信息："+e.getMessage());
            }
        }
    }

    public void test(){
        try {
            String code = "600050";
            List<Map<String,Object>> list1 = ci.financeInfoParse(usedMethod.getQuesMethod(FinanceInfoUrl(0,code)),code,0);
            List<Map<String,Object>> list2 = ci.financeInfoParse(usedMethod.getQuesMethod(FinanceInfoUrl(1,code)),code,1);
            List<Map<String,Object>> list3 = ci.financeInfoParse(usedMethod.getQuesMethod(FinanceInfoUrl(2,code)),code,2);
            List<Map<String,Object>> list4 = usedMethod.mergeListMap(list1,list2);
            List<Map<String,Object>> list5 = usedMethod.mergeListMap(list4,list3);
            System.out.println(list5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CompanyInfoParse c = new CompanyInfoParse();
        UsedMethod u = new UsedMethod();
        try {
            String code = "600050";
            c.financeInfoParse(u.getQuesMethod(FinanceInfoUrl(2,code)),code,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
