package com.isoftstone.common.api.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocExport {

    @Autowired
    CommonDb commonDb;

    private Configuration configuration = null;

    public DocExport() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
    }

    /**
     * 注意dataMap里存放的数据Key值要与模板中的参数相对应
     *
     * @param dataMap
     */
    private Map getData(Map dataMap) {
        dataMap.put("title", "我是个标题");
        List<Map<String, Object>> newsList = new ArrayList();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("num", i);
            map.put("what", "第" + i + "个");
            map.put("detial", "我是第" + i + "个，请看我！");
            newsList.add(map);
        }
        dataMap.put("newsList", newsList);
        List<Map<String, Object>> imgsList = new ArrayList<>();
        for (int j = 1; j <= 2; j++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            if (j == 1) {
                map1.put("img", getImageStr("C:\\Users\\kraus\\Desktop\\work文件\\公司wifi账户密码.jpg"));
            } else {
                map1.put("img", getImageStr("C:\\Users\\kraus\\Desktop\\work文件\\数据流程.png"));
            }
            imgsList.add(map1);
        }
        dataMap.put("imgsList", imgsList);
        return dataMap;
    }

    private String getImageStr(String imgPath) {
        String imgFile = imgPath;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * @param ftlPath      word模版路径 ps D://
     * @param ftlName      word模版名称（含后缀名） ps temple.ftl
     * @param docSavePatch 生成的word保存路径 ps D:/生成的第一个word.doc
     * @param dataMap      需要生成到word的数据
     */
    public void createDoc(String ftlPath, String ftlName, String docSavePatch, Map dataMap) {
        Template t = null;
        try {
            // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
            // 这里我们的模板是放在com.template包下面
            configuration.setDirectoryForTemplateLoading(new File(ftlPath));
            // test.ftl为要装载的模板
            t = configuration.getTemplate(ftlName);
            t.setEncoding("utf-8");
            File outFile = new File(docSavePatch);
            Writer out = null;
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile), "utf-8"));
            t.process(dataMap, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> putDataToMap(String id, Map<String, Object> map) {
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("pid", id);
        List<Map<String, Object>> list = commonDb.selectMethod("S12036", paraMap);
        List<Map<String, Object>> list1 = new ArrayList<>();
        if (list.size() > 0) {
            for (Map<String, Object> m : list) {
                Map<String, Object> m1 = new HashMap<>();
                String menuId = m.get("menu_id").toString();
                m1.put("title", m.get("menu_name").toString());
                List<Map<String, Object>> list2 = this.putDataToMap(menuId, m1);
                if (list2.size() > 0) {
                    m1.put("Lists", list2);
                } else {
                    paraMap.put("menu_id", menuId);
                    List<Map<String, Object>> list3 = commonDb.selectMethod("S12038", paraMap);
                    System.out.println(menuId);
                    try {
                        map.put("info", list3.get(0).get("content").toString().replaceAll("[^\\u4e00-\\u9fa5]", ""));
                    } catch (Exception e) {
                        map.put("info", "");
                    }
                }
                list1.add(m1);
            }
        }
        System.out.println(map.toString());
        return list1;
    }

    public Map getDbData() {
        System.out.println("输出" + 1);
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("pid", "0");
        List<Map<String, Object>> list = commonDb.selectMethod("S12036", paraMap);
        System.out.println(list.toString());
        String menuId = list.get(list.size() - 1).get("menu_id").toString();
        String menuName = list.get(list.size() - 1).get("menu_name").toString();
        paraMap.put("title", menuName);
        paraMap.put("List", putDataToMap(menuId, paraMap));
        System.out.println(paraMap.toString());
        return paraMap;
    }

    public void run() {
        // 要填入模本的数据文件
        createDoc("D://", "temple.ftl", "D:/word.doc", getDbData());
        System.out.println("end");
    }
}
