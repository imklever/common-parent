package com.isoftstone.common.networm.parse;

import com.isoftstone.common.networm.util.UsedMethod;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Component
@Repository
public class CompanyInfoParse {
    @Autowired
    UsedMethod usedMethod;

    /**
     * 行业列表
     */
    public List<Map<String,Object>> GradeTypeParse(Document resp){
        JSONArray jo = JSONArray.fromObject(resp.body().text());
        List<Map<String,Object>> HYlist = new ArrayList<Map<String,Object>>();
        List list = (List) jo;
        List XlHY = (List) list.get(1);
        List xlHY = (List) ((List) XlHY.get(0)).get(1);
        try{
            List xlHY1 = (List)((List) xlHY.get(1)).get(1);
            System.out.println(xlHY1);
            for (int j=0;j<xlHY1.size();j++) {
                Map<String,Object> map = new HashMap<String,Object>();
                List a = (List) xlHY1.get(j);
                if ("".equals(a.get(1))) {
                    map.put("Grade_Type",a.get(2).toString());
                    map.put("Grade_Type_Name",a.get(0).toString());
                    HYlist.add(map);
                }
//                else {
//                    List xlHY2 = (List) ((List) xlHY1.get(j)).get(1);
//                    for (int m = 0; m < xlHY2.size(); m++) {
//                        HYlist.add(((List) xlHY2.get(m)).get(2).toString());
//                    }
//                }
            }
        }catch(Exception e){
            return HYlist;
        }
        return HYlist;
    }

    public int getInfoListCountParse(Document countResp){
        String count = countResp.body().text().replaceAll("\\D", "");
        return Integer.parseInt(count);
    }

    /**
     * 所属行业公司信息列表
     */
    public List<String> CompanyInfoListParse(Document infoResp){
        if(infoResp == null){
            return new ArrayList<>();
        }else {
            JSONArray jo = JSONArray.fromObject(infoResp.body().text());
            List<String> list = new ArrayList<String>();
            List<Map<String,String>> companyList = (List<Map<String,String>>) jo;
            for (Map<String,String> company :companyList){
                String sysmbol = company.get("symbol");
                String code = company.get("code");
                String name = company.get("name");
                list.add(sysmbol+","+code+","+name);
            }
            return list;
        }
    }

    /**
     * 返回company对象集合
     * @param resp
     * @return
     */
    public Map<String,Object> CommanyDetialInfoParse(String hy,String a,Document resp) throws Exception{
        Map<String,Object> company = new HashMap<String, Object>();
        String[] aa = a.split(",");
        company.put("GradeType",hy);
        company.put("simpleName",aa[2]);
        company.put("compCode",aa[1]);
        company.put("compCode1",aa[0]);
        Element body = resp.body();
        Elements trs = body.getElementById("comInfo1").select("tr");
        for(int i=0;i<trs.size();i++){
            Elements td = trs.get(i).select("td");
            switch (i){
                case 0: company.put("compName",td.get(1).text());
                        if("".equals(td.get(1).text())){
                            return company;
                        }else{
                            break;
                        }
                case 1: company.put("enName",td.get(1).text());break;
                case 2: company.put("ssbk",td.get(1).text());
                            company.put("ssrq",td.get(3).select("a").text());break;
                case 4: company.put("clrq",td.get(1).select("a").text());
                            String dt4 = td.get(3).text();
                            company.put("zczb",usedMethod.String2Double(dt4));
                            break;
                case 5: company.put("zzxs",td.get(3).text());break;
                case 6: company.put("gsdh",td.get(3).text());break;
                case 12: company.put("gswz",td.get(3).text());break;
                case 14: company.put("yzbm",td.get(1).text());break;
                case 17: company.put("zcdz",td.get(1).text());break;
                case 19: company.put("gsjj",td.get(1).text());break;
                case 20: company.put("jyfw",td.get(1).text());break;
                default:;

            }
        }
        return company;
    }
    public List<String> yearListParse(Document document){
        List<String> list = new ArrayList<String>();
        Elements ele = document.body().getElementById("con02-1").select("td").select("a");
        for(int i=0;i<ele.size()-1;i++){
            String year = ele.get(i).text();
            list.add(year);
        }
        return list;
    }

    public static Map<String,String> getMathMapping(String mappingStr){
        Map<String,String> mappingMap = new HashMap<>();
        JSONObject jo1 = JSONObject.fromObject(mappingStr);
        Map<String,Object> map1 = (Map<String,Object>) jo1;
        JSONArray ja = JSONArray.fromObject(map1.get("FontMapping"));
        List<Map<String,Object>> mappingList = (List<Map<String,Object>>) ja;
        for(Map<String,Object> m : mappingList){
            String key = m.get("code").toString();
            String value = m.get("value").toString();
            mappingMap.put(key,value);
        }
        return mappingMap;
    }

    public List<Map<String,Object>> financeInfoParse(Document doc,String companyCode,int type) throws Exception{
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        if(doc != null){
            JSONObject jo = JSONObject.fromObject(doc.text());
            Map<String,Object> map = (Map<String,Object>) jo;
            Map<String,String> mappingMap = getMathMapping(map.get("font").toString());
            String data = map.get("data").toString();
            Set keySet = mappingMap.keySet();
            for(Object keyObj : keySet){
                String key = keyObj.toString();
                data = data.replace(key,mappingMap.get(key).toString());
            }
            JSONArray ja1 = JSONArray.fromObject(data);
            List<Map<String,Object>> dataList = (List<Map<String,Object>>) ja1;
            for(Map<String,Object> m : dataList){
                Map<String,Object> mm = new HashMap<>();
                mm.put("compCode",companyCode);
                String quarter = m.get("reportdate").toString().split("T")[0];
                mm.put("quarter",quarter);
                String year = quarter.substring(0,4);
                mm.put("year",year);
                switch (type){
                    case 0 :
                        mm.put("jlr",usedMethod.String2Double(m.get("parentnetprofit").toString()));
                        mm.put("jlrtb",usedMethod.String2Double(m.get("sjltz").toString()));
                        mm.put("kfgmjlr",usedMethod.String2Double(m.get("kcfjcxsyjlr").toString()));
                        mm.put("kfgmjlrtb",usedMethod.String2Double(m.get("sjlktz").toString()));
                        mm.put("yyzsr",usedMethod.String2Double(m.get("totaloperatereve").toString()));
                        mm.put("yyzsrtb",usedMethod.String2Double(m.get("tystz").toString()));
                        mm.put("yyzc",usedMethod.String2Double(m.get("yyzc").toString()));
                        mm.put("yyzctb",usedMethod.String2Double(m.get("operateexp_tb").toString()));
                        mm.put("xsfy",usedMethod.String2Double(m.get("saleexp").toString()));
                        mm.put("glfy",usedMethod.String2Double(m.get("manageexp").toString()));
                        mm.put("cwfy",usedMethod.String2Double(m.get("financeexp").toString()));
                        mm.put("yyzzc",usedMethod.String2Double(m.get("totaloperateexp").toString()));
                        mm.put("yyzzctb",usedMethod.String2Double(m.get("totaloperateexp_tb").toString()));
                        mm.put("yylr",usedMethod.String2Double(m.get("operateprofit").toString()));
                        mm.put("yylrtb",usedMethod.String2Double(m.get("yltz").toString()));
                        mm.put("lrze",usedMethod.String2Double(m.get("sumprofit").toString()));
                        break;
                    case 1 :
                        mm.put("zzc",usedMethod.String2Double(m.get("sumasset").toString()));
                        mm.put("zzctb",usedMethod.String2Double(m.get("tsatz").toString()));
                        mm.put("gdzc",usedMethod.String2Double(m.get("fixedasset").toString()));
                        mm.put("yszk",usedMethod.String2Double(m.get("accountrec").toString()));
                        mm.put("ch",usedMethod.String2Double(m.get("inventory").toString()));
                        mm.put("chtb",usedMethod.String2Double(m.get("inventory_tb").toString()));
                        mm.put("zfz",usedMethod.String2Double(m.get("sumliab").toString()));
                        mm.put("zfztb",usedMethod.String2Double(m.get("tdetz").toString()));
                        break;
                    case 2 :
                        mm.put("mgsy",usedMethod.String2Double(m.get("basiceps").toString()));
                        mm.put("mgjzc",usedMethod.String2Double(m.get("bps").toString()));
                        mm.put("jzcsyl",usedMethod.String2Double(m.get("roeweighted").toString()));
                        mm.put("xsmll",usedMethod.String2Double(m.get("xsmll").toString()));
                        break;
                    default:
                        break;
                }

                listMap.add(mm);
            }
        }
        return listMap;
    }

    public List<Map<String,Object>> financeBaseInfoParse(Document doc) throws Exception{
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        JSONArray jo = JSONArray.fromObject(doc.body().text());
        List<Map<String,Object>> list = (List<Map<String,Object>>) jo;
        for(Map<String,Object> m :list){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("quarter",m.get("date").toString());
            map.put("mlr",usedMethod.String2Double(m.get("mlr").toString()));
            map.put("yyzsr",usedMethod.String2Double(m.get("yyzsr").toString()));
            map.put("zyywsrzzl",usedMethod.String2Double(m.get("yyzsrtbzz").toString()));
            map.put("xsmll",usedMethod.String2Double(m.get("mll").toString()));
            map.put("xsjll",usedMethod.String2Double(m.get("jll").toString()));
            map.put("zzczzts",usedMethod.String2Double(m.get("zzczzy").toString()));
            map.put("chzzts",usedMethod.String2Double(m.get("chzzts").toString()));
            map.put("yszkzzts",usedMethod.String2Double(m.get("yszkzzts").toString()));
            map.put("zcfzl",usedMethod.String2Double(m.get("zcfzl").toString()));
            listMap.add(map);
        }
        return listMap;
    }

    public List<Map<String,Object>> financeBFBInfoParse(Document doc)throws Exception{
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        JSONObject jo = JSONObject.fromObject(doc.body().text());
        List<Map<String,Object>> list = (List<Map<String,Object>>)((Map<String,Object>)jo.get("Result")).get("lr0");
        for(Map<String,Object> m :list){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("quarter",m.get("date").toString());
            map.put("yycb",usedMethod.String2Double(m.get("yycb").toString()));
            map.put("yysjjfj",usedMethod.String2Double(m.get("yysjjfj").toString()));
            map.put("xsfy",usedMethod.String2Double(m.get("xsfy").toString()));
            map.put("glfy",usedMethod.String2Double(m.get("glfy").toString()));
            map.put("cwfy",usedMethod.String2Double(m.get("cwfy").toString()));
            map.put("jlr",usedMethod.String2Double(m.get("jlr").toString()));
            listMap.add(map);
        }
        return listMap;
    }

    public List<Map<String,Object>> financeZCBBInfoParse(Document doc)throws Exception{
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        JSONObject jo = JSONObject.fromObject(doc.body().text());
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> m = (Map<String,Object>)jo.get("zb");
        map.put("quarter",m.get("date").toString());
        map.put("gdzcje",usedMethod.String2Double(m.get("gdzc").toString()));
        map.put("wxzc",usedMethod.String2Double(m.get("wxzc").toString()));
        listMap.add(map);
        return listMap;
    }



}
