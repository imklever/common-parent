package com.isoftstone.common.backup.service.lucene;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author ltzhangg
 *	此接口是用反射的形式调用,作用是查询数据库中的某字段,进行字段的内容分词,可存储在数据库中
 */
public interface AnalyzeWord {
	
	
	public List<Map<String,Object>> analyzeWord(Map<String, Object> map);
}
