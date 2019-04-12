package com.isoftstone.common.networm;

import com.isoftstone.common.networm.mapper.VisuaSqlExampleMapper;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.service.CommBusinessRunService;
import com.isoftstone.common.util.JsonService;
import net.sf.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CDHTest {

    @Autowired
    JsonService jsonService;
    @Autowired
    CommBusinessRunService commBusinessRunService;
    @Autowired
    VisuaSqlExampleMapper visuaSqlExampleMapper;

    @Test
    public void test(){
        VisuaSqlExample visuaSqlExample = visuaSqlExampleMapper.getByBusinessCode("S12037");
        Map<String, Object> mapKeyWord = null;
        try {
            Map<String,Object> paraMap = new HashMap<>();
            long start = System.currentTimeMillis();
            for(int i=0;i<100;i++){
                paraMap.put("tableName","wyphao");
                paraMap.put("id",i);
                paraMap.put("text","人工智能"+i);
                mapKeyWord = commBusinessRunService.getByParamBusinessCode(visuaSqlExample, jsonService.toJson(paraMap), null, null);
            }
            System.out.println("耗时：" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mapKeyWord);
//        list = (List<Map<String, Object>>) JSONArray.fromObject(mapKeyWord.get("datalist"));
    }
}
