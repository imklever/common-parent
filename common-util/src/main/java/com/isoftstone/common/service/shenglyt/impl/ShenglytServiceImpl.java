package com.isoftstone.common.service.shenglyt.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
//import com.isoftstone.ai.slytai.AIInterface;
//import com.isoftstone.ai.slytai.APIConstants;
import com.isoftstone.common.service.shenglyt.ShenglytService;
@Service
public class ShenglytServiceImpl  implements ShenglytService{
	
	/**
	 * 构造数据 油井实时数据
	 */
	@Override
	public List<Map<String, Object>> getYjScores(Map<String, Object> map) {
		return null;
	}
    /**
    * 水井实时数据
    */
	@Override
	public List<Map<String, Object>> getSjScores(Map<String, Object> map) {
	
		return null;
	}
    /**
     * 油井功图数据
     */
	@Override
	public List<Map<String, Object>> getGtScores(Map<String, Object> map) {
		
		return null;
	}
}
