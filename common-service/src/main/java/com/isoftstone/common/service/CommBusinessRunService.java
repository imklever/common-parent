package com.isoftstone.common.service;

import java.util.Map;

import com.isoftstone.common.plugins.visua.VisuaSqlExample;

public interface CommBusinessRunService {
     Map<String, Object> getByParamBusinessCode(VisuaSqlExample visuaSqlExample, String params,String name,String type) throws Exception ;
}
