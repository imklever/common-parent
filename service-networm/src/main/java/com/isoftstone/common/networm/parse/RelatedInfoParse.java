package com.isoftstone.common.networm.parse;

import com.isoftstone.common.networm.util.UsedMethod;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Repository
public class RelatedInfoParse {
    @Autowired
    UsedMethod usedMethod;
    public int sinaContentNumParse(Document doc){
        int num = 0;
        try{
            if (doc != null){
                Elements ele = doc.getElementsByClass("tips_01");
                if ("".equals(ele.toString())){
                    num = Integer.parseInt(doc.getElementsByClass("l_v2").text().replaceAll("[^0-9]",""));
                }
            }
        }catch(Exception e){
            return num;
        }
        return num;
    }
    /**
     * 新浪文章信息url解析
     * @param doc
     * @return
     */
    public List<String> sinaContentInfoUrlParse(Document doc){
        List<String> urlList = new ArrayList<>();
        if(doc != null){
            Elements divList = doc.getElementsByClass("r-info r-info2");
            for (int i=0;i<divList.size();i++){
                String oneUrl = divList.get(i).select("a").attr("href");
                urlList.add(oneUrl);
            }
        }
        return urlList;
    }
    public Map<String,Object> sinaContentInfoParse(String url,Document doc){
        Map<String,Object> map = new HashMap<>();
        if (doc != null){
            Pattern p = null;
            Matcher m = null;
            p = Pattern.compile(" [P?p?]ublished.*at (.*) [From?from?\\]?]");
            m = p.matcher(doc.toString());
            String time = "";
            if(m.find()) {
                time = m.group(1);
            }else{

            }
            String newscode = "";
            p = Pattern.compile("docID: 'http://doc.sina.cn/\\?id=comos:(.+)',");
            m = p.matcher(doc.select("script[type=text/javascript]").toString());
            if (!"".equals(doc.select("meta[name=publishid]").toString())){
                newscode = doc.select("meta[name=publishid]").attr("content");
            }else if(m.find()){
                newscode = m.group(1);
            }else{
                p = Pattern.compile("http://.*/(.+)\\.shtml");
                m = p.matcher(url);
                while (m.find()){
                    newscode = m.group(1);
                }
            }
            String title = "";
                if (!"".equals(doc.select("meta[property=og:title]").toString())){
                title = doc.select("meta[property=og:title]").attr("content");
            }else if(!"".equals(doc.getElementsByClass("main_title").toString())){
                title = doc.getElementsByClass("main_title").text();
            }else if(!"".equals(doc.getElementsByClass("main-title").toString())){
                title = doc.getElementsByClass("main-title").text();
            }else if(doc.getElementById("artibodyTitle") != null){
                title = doc.getElementById("artibodyTitle").select("h1").text();
                if("".equals(title))
                    title = doc.getElementById("artibodyTitle").text();
            }else if(doc.getElementById("article") != null){
                title = doc.getElementById("article").select("tr").select("th").select("font[color=#05006C]").text();
            }else{
                title = doc.getElementsByTag("title").text();
            }
            String content = "";
            if(doc.getElementById("artibody") != null){
                content = doc.getElementById("artibody").text().trim();
            }else if(doc.getElementById("article") != null){
                content = doc.getElementById("article").text().trim();
            }else if(!"".equals(doc.getElementsByClass("article").toString())){
                content = doc.getElementsByClass("article").text().trim();
            }else if(!"".equals(doc.getElementsByClass("content").toString())){
                content = doc.getElementsByClass("content").text().trim();
            }else{
                content = doc.getElementsByTag("table").select("p").text();
            }
            String author = "";
            if (!"".equals(doc.select("meta[name=mediaid]").toString())) {
                author = doc.select("meta[name=mediaid]").attr("content");
            }else if(!"".equals(doc.getElementsByClass("source").toString())){
                author = doc.getElementsByClass("source").text();
            }else if(doc.getElementById("article") != null){
                author = doc.getElementById("article").select("font[color=#A20010]").text();
            }else if (!"".equals(doc.getElementsByClass("from_info").toString())){
                author = doc.getElementsByClass("from_info").select("a").text();
                if ("".equals(author))
                    author = doc.getElementsByClass("from_info").select("font[color=#A20010]").text();
            }else if(doc.getElementById("media_name") != null){
                author = doc.getElementById("media_name").text();
            }else if (!"".equals(doc.getElementsByClass("artInfo").toString())){
                author = doc.getElementsByClass("artInfo").select("a[target=_blank]").text();
            }else if(!"".equals(doc.select("link[rel=alternate]").toString())){
                author = doc.select("link[rel=alternate]").attr("title");
            }else{
                author = doc.getElementsByTag("table").select("font[color=#A20010]").text();
            }
            String keywords = doc.select("meta[name=keywords]").attr("content");
            map.put("url",url);
            map.put("source","sina");
            map.put("newscode",newscode);
            map.put("title",title);
            map.put("release_time",time);
            map.put("content",content.replace("　",""));
            map.put("publisher",author);
            map.put("keyword",keywords);
        }
        return map;
    }

    public int ofWeekContentNumParse(Document doc){
        int num = 0;
        try{
            Elements numList = doc.getElementsByClass("num").select("a");
            num = Integer.parseInt(numList.get(numList.size()-2).text());
            return num;
        }catch(Exception e){
            return num;
        }
    }

    public List<String> ofWeekContentInfoUrlParse(Document doc){
        List<String> list = new ArrayList<>();
        if(doc != null){
            Elements urlList = doc.getElementsByClass("zixun").select("a");
            for(Element url : urlList){
                list.add(url.attr("href"));
            }
        }
        return list;
    }

    private String pageContent(Document doc){
        return doc.getElementById("articleC").text();
    }

    public Map<String,Object> ofWeekContentInfoParse(String url,Document doc){
        Map<String,Object> map = new HashMap<>();
        try {
            String time = "";
            if(!"".equals(doc.getElementsByClass("sdate").toString())){
                time = doc.getElementsByClass("sdate").text();
            }
            String newscode = "";
            if (doc.getElementById("detailId") != null) {
                newscode = doc.getElementById("detailId").attr("value");
            }else if (doc.getElementById("pagenewsdetailDetailid") != null){
                newscode = doc.getElementById("pagenewsdetailDetailid").attr("value");
            }
            String title = "";
            if (!"".equals(doc.getElementsByTag("h1").toString())){
                title = doc.getElementsByTag("h1").text();
            }
            String author = "";
            if(doc.getElementById("laiyuan_mp") != null){
                author = doc.getElementById("laiyuan_mp").select("a").attr("title");
            }else if(doc.getElementById("laiyuan") != null){
                author = doc.getElementById("laiyuan").text();
            }
            String keywords = doc.select("meta[name=Keywords]").attr("content");
            String leader = doc.getElementsByClass("simple").text();
            String content = leader + pageContent(doc);
            Elements pageClass = doc.getElementsByClass("page");
            if(!"".equals(pageClass.toString())){
                for (int i=1;i<pageClass.select("a").size()-1;i++){
                    String nextUrl = url.replace(newscode,newscode+ "_" + pageClass.select("a").get(i).text());
                    String nextContent =  pageContent(usedMethod.getQuesMethod(nextUrl));
                    content += nextContent;
                }
            }
            map.put("url",url);
            map.put("source","ofWeek");
            map.put("newscode",newscode);
            map.put("title",title);
            map.put("release_time",time);
            map.put("content",content.replace("",""));
            map.put("publisher",author);
            map.put("keyword",keywords);
            return map;
        }catch (Exception e){
            return map;
        }
    }
}
