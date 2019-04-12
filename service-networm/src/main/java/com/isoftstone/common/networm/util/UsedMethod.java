package com.isoftstone.common.networm.util;

import com.isoftstone.common.networm.db.CommonDB;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Repository
public class UsedMethod {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommonDB commonDB;

    private String code = "";

    public void setCode(String code) {
        this.code = code;
    }

    private static String[] ua =
            {
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"
            };

    private boolean checkIp(String ip, String port) {
        try {
            Jsoup.connect("https://www.baidu.com")
                    .timeout(5000)
                    .proxy(ip, Integer.parseInt(port))
                    .get().text();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * 将有效代理插入ip代理池
     */
    public void saveProxy() {
        String[] codes = this.getModuleCodes("saveProxy",code);
        if(codes != null){
            String GET_IP_URL = "https://www.kuaidaili.com/free/inha/" + (int) (1 + Math.random() * 10);
            Random random = new Random();
            int a = random.nextInt(14);
            List<Map<String,Object>> list = new ArrayList<>();
            try {
                Elements body  = Jsoup.connect(GET_IP_URL)
                        .timeout(5000)
                        .ignoreContentType(true)
                        .userAgent(ua[a])
                        .header("referer", GET_IP_URL)
                        .get().getElementsByTag("tbody");
                Elements trs = body.select("tr");
                for (int i = 0; i < trs.size(); i++) {
                    Elements td = trs.get(i).select("td");
                    Map<String,Object> proxyMap = new HashMap<>();
                    if (checkIp(td.get(0).text(), td.get(1).text())) {
                        proxyMap.put("proxy_ip",td.get(0).text());
                        proxyMap.put("proxy_port",td.get(1).text());
                        list.add(proxyMap);
                    } else {
                        continue;
                    }
                }
                logger.info("代理抓取："+ GET_IP_URL);
                commonDB.IsInsertOrUpdateList(list,codes[0],codes[1],codes[2]);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 设置代理
     */
    private String setProxy(){
        String proxy = "";
        String [] codes = getModuleCodes("setProxy",code);
        if(codes != null){
            try{
                List<Map<String,Object>> proxyList = commonDB.selectMethod(codes[0],new HashMap<>());
                if (proxyList.size() > 0){
                    Random r = new Random();
                    int b = r.nextInt(proxyList.size());
                    String ip = proxyList.get(b).get("proxy_ip").toString();
                    String port = proxyList.get(b).get("proxy_port").toString();
                    int count = 0;
                    boolean flag = false;
                    while (count < 5){
                        count ++;
                        flag = checkIp(ip,port);
                        if (flag){
                            count = 100;
                        }
                    }
                    if(flag){
                        proxy = ip + ":" +port;
                        logger.info("代理设置成功，代理为:" + ip +":"+port);
                    }else {
                        Thread.sleep(threadSleepTime(5000));
                        String id = proxyList.get(b).get("id").toString();
                        Map<String,Object> paraMap = new HashMap<>();
                        paraMap.put("id",id);
                        commonDB.updateMethond(paraMap,codes[1]);
                        logger.info("代理:" + ip +":"+port+"已经失效，不使用代理请求。");
                    }
                }else{
                    Thread.sleep(threadSleepTime(5000));
                    logger.info("暂无可用代理，不使用代理请求。");
                }
            }catch(Exception e){
                logger.error("设置代理时:"+ e.getMessage());
            }
        }
        return proxy;
    }

    public void setProxyService(){
        String ip = "114.116.1.247";
        String port = "8118";
        if(checkIp(ip,port)){
            System.setProperty("http.proxyHost", ip);
            System.setProperty("http.proxyPort", port);
            System.setProperty("https.proxyHost", ip);
            System.setProperty("https.proxyPort", port);
            logger.info("全局代理--代理服务器："+ip+":"+port+"代理成功！！！");
        }else{
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
            System.clearProperty("https.proxyHost");
            System.clearProperty("https.proxyPort");
            logger.info("全局代理--代理服务器："+ip+":"+port+"无法连接网络，清除全局代理。请检查代理服务器！！！");
        }
    }

    /**
     *
     * 普通请求通用函数
     * @return 请求之后的结果
     */
    public Document getQuesMethod(String url){
        setProxyService();
        String proxy = "";
        if(!"".equals(code)){
            proxy =  setProxy();
        }
        Random r = new Random();
        int b = r.nextInt(14);
        Document response = null;
        try{
            if ("".equals(proxy)) {
                response = Jsoup.connect(url)
                        .timeout(20000)
                        .ignoreContentType(true)
                        .userAgent(ua[b])
                        .header("referer", url)
                        .validateTLSCertificates(false)
                        .get();
            }else {
                String[] pro = proxy.split(":");
                response = Jsoup.connect(url)
                        .timeout(20000)
                        .proxy(pro[0],Integer.parseInt(pro[1]))
                        .ignoreContentType(true)
                        .userAgent(ua[b])
                        .header("referer", url)
                        .validateTLSCertificates(false)
                        .get();
            }
            logger.info(url+ ",请求成功");
        } catch (Exception e){
            logger.error(url+"，未请求到数据" + e);
        }
        return response;
    }

    /**
     * 合并Map<String,Object>
     */
    private <K, V> Map mergeMaps(Map<K, V>... maps) {
        Class clazz = maps[0].getClass(); // 获取传入map的类型
        Map<K, V> map = null;
        try {
            map = (Map) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0, len = maps.length; i < len; i++) {
            map.putAll(maps[i]);
        }
        return map;
    }
    /**
     * 合并两个List<Map<String,Object>> 集合。
     */
    public List<Map<String,Object>> mergeListMap(List<Map<String,Object>> a,List<Map<String,Object>> b){
        for(Map<String,Object> m : b){
            boolean flag = false;
            String year = m.get("quarter").toString();
            for(int i= 0;i<a.size();i++){
                String year1 = a.get(i).get("quarter").toString();
                if(year.equals(year1)){
                    Collections.replaceAll(a,a.get(i),mergeMaps(a.get(i),m));
                    flag = true;
                }
            }
            if (!flag){
                a.add(m);
            }
        }
        return a;
    }

    private double TwoDecimalPlaces(double number) {
        BigDecimal b = new BigDecimal(Double.toString(number));
        return b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 爬取的数据中字段 含有亿，万,%字段改为double类型
     */
    public double String2Double(String str){
        String last = str.replaceAll("[^\\-0-9\\.]","");
        if(str.indexOf("万亿")!= -1){
            return TwoDecimalPlaces(Double.parseDouble(last) * 1000000000000.00);
        }else if(str.indexOf("亿")!= -1){
            return TwoDecimalPlaces(Double.parseDouble(last) * 100000000.00);
        }else if(str.indexOf("万")!= -1) {
            return TwoDecimalPlaces(Double.parseDouble(last) * 10000.00);
        }else if(str.indexOf("%") != -1){
            return TwoDecimalPlaces(Double.parseDouble(last) / 100.00);
        }else if(str.indexOf("--") != -1){
            return 0.00;
        }else if("-".equals(str)){
            return 0.00;
        }else if(str.indexOf("E") != -1){
            String num = str.split("E")[0];
            String bit = str.split("E")[1];
            double number = Double.parseDouble(num) * Math.pow(10,Double.parseDouble(bit));
            return TwoDecimalPlaces(number);
        }else{
            return TwoDecimalPlaces(Double.parseDouble(last) * 1.00);
        }
    }

    /**
     * @return 返回一个线程随眠时间，20秒之内随机
     */
    public int threadSleepTime(int hmz){
        Random r = new Random();
        return r.nextInt(hmz);
    }

    /**
     * 返回经过正则处理过的字符串，无匹配返回空字符串
     * @param regex 正则表达式
     * @param findStr 需要查找的字符串
     * @return 结果
     */
    public String getRegexAfterStr(String regex,String findStr){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(findStr);
        String str = "";
        if(m.find()) {
            str = m.group(1);
        }
        return str;
    }

    public List<Element> fixedGrouping(Elements source, int n) {
        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<Element> result = new ArrayList<>();
        int sourceSize = source.size();
        int size = (source.size() / n) + 1;
        for (int i = 0; i < size; i++) {
            Element subset = new Element("div");
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.append(source.get(j).toString());
                }
            }
            result.add(subset);
        }
        result.remove(result.size()-1);
        return result;
    }

    public String [] getModuleCodes(String module,String code){
        Map<String,Object> map = new HashMap<>();
        map.put("module",module);
        List<Map<String,Object>> list = commonDB.selectMethod(code,map);
        if (list.size() == 1){
            return list.get(0).get("businessCodes").toString().split(",");
        }else{
            logger.info(module+"模块中businessCodes不存在或不唯一！请更改");
            return null;
        }
    }

    public String getTargetTable(Map map,String codes,String whichTable){
        String targetTable = "";
        List<Map<String,Object>> list = commonDB.selectMethod(codes,map);
        if (list.size() == 1){
            targetTable = list.get(0).get(whichTable).toString();
        }else{
            logger.info(map+",中"+whichTable+"不存在或不唯一，请查看网站表target_table");
        }
        return targetTable;
    }

    public String getQuesUrl(String type,List<String> param){
        String url = "";
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("type",type);
        List<Map<String,Object>> list = commonDB.selectMethod("S12060",paraMap);
        if(list.size() == 1){
            int paramNum = Integer.parseInt(list.get(0).get("parameter").toString());
            String uurl = list.get(0).get("url").toString();
            for(int i=0;i<=paramNum;i++){
                if(i==0){
                    url = uurl;
                }else{
                    url = url.replace("${param"+ i +"}",param.get(i-1));
                }
            }
        }
        return url;
    }

    public static void main(String[] args) {
    }
}
