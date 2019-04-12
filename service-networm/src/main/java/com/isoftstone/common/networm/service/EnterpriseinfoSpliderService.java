package com.isoftstone.common.networm.service;

import java.util.List;
import java.util.Map;

public interface EnterpriseinfoSpliderService {
    String getData(String url);
    List<Map<String,String>> getList(String data);
    Map<String,Object> getDataMap(String data);
}
