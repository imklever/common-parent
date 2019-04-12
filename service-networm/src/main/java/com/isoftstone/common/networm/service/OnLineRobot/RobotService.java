package com.isoftstone.common.networm.service.OnLineRobot;

import com.isoftstone.common.networm.db.CommonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RobotService {

    @Autowired
    CommonDB commonDB;

    public Map<String,Object> getAnswers(String message,String answerType,String robotType){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("ask",message);
        paraMap.put("answerType",answerType);
        paraMap.put("robotType",robotType);
        List<Map<String,Object>> list = commonDB.selectMethod("S12049",paraMap);
        String msg = "";
        String title = "";
        int answer_id = 0;
        if(list.size() >= 1) {
            title = list.get(0).get("questions").toString();
            msg = list.get(0).get("answers").toString();
            answer_id = Integer.parseInt(list.get(0).get("id").toString());
        }
        map.put("title",title);
        map.put("msg",msg);
        map.put("answer_id",answer_id);
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("ask",message);
        insertMap.put("answer",msg);
        insertMap.put("count",list.size());
        insertMap.put("robotType",robotType);
        commonDB.insertMethod(insertMap,"S12048");
        System.out.println(map.toString());
        return map;
    }

    public List<String> getTipsList(String message,String answerType,String robotType){
        List<String> list = new ArrayList<>();
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("ask",message);
        paraMap.put("answerType",answerType);
        paraMap.put("robotType",robotType);
        List<Map<String,Object>> answerList = commonDB.selectMethod("S12047",paraMap);
        for(Map<String,Object> m : answerList){
            list.add(m.get("questions").toString());
        }
        System.out.println(list);
        return list;
    }

}
