package com.isoftstone.common.api.generate;

import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommonDb {
    @Autowired
    JsonService jsonService;
    @Autowired
    HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;

    public List<Map<String,Object>> selectMethod(String cxdm, Map<String,Object> paraMap){
        List<Map<String,Object>> list = new ArrayList<>();
        Map map = hystrixVisuaSqlExampleClient.getByDataBusinessCode(cxdm,jsonService.toJson(paraMap));
        list = (List<Map<String, Object>>) JSONArray.fromObject(map.get("datalist"));
        return list;
    }


}
