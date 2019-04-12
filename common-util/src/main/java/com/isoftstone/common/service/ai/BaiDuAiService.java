package com.isoftstone.common.service.ai;


import java.util.List;
import java.util.Map;
	
public interface BaiDuAiService {
	//图片识别文字
	 Object freeImageOCR(Map<String,Object>map);
	//英文翻译
	 List<Map<String, Object>> getTransResult(Map<String,Object>map);
	 
	 //按列名翻译
	List<Map<String, Object>> transColumn(Map<String, Object> map);

}
