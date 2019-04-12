package com.isoftstone.common.networm;

import com.isoftstone.common.networm.db.CommonDB;
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

public class SplitData {
    @Autowired
    CommonDB commonDB;

    @Test
    public void test(){
        Map map = new HashMap();
        map.put("info","sys_%");
        List<Map<String,Object>> list = commonDB.selectMethod("S12045",map);
        for(Map<String,Object> m : list){
            String tableName = m.get("table_name").toString();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("tableName",tableName);
            List<Map<String,Object>> list1 = commonDB.selectMethod("S12046",map1);
            if(list1.size() == 1){
                System.out.println(list1.get(0).get("Create Table"));
            }
        }
    }
}
