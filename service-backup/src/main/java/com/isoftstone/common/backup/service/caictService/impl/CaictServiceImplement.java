package com.isoftstone.common.backup.service.caictService.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.backup.service.caictService.CaictService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.util.JsonService;

@Service
public class CaictServiceImplement implements CaictService {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	JsonService jsonService;
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;

	@SuppressWarnings("unchecked")
	@Override
	public void syschronize(Map<String, Object> map) {
		int count = 600;
		if (map != null && map.containsKey("url")) {
			@SuppressWarnings("rawtypes")
			ResponseEntity<Map> postForEntity = null;
			Map<String, List<Map<String, Object>>> body = null;
			List<Map<String, Object>> list = null;
			// 獲取sql返回的 数据List<Map<String, Object>>
			List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("url");
			System.out.println(maplist.size());
			// 遍历拿出对应 的 ip，url，port
			for (Map<String, Object> newmap : maplist) {
				String ip = (String) newmap.get("ip");
				String port = (String) newmap.get("port");
				String url = (String) newmap.get("u");
				String business_code = (String) newmap.get("business_code");
				if (url != null && url.length() > 0) {
					
					while (count>50) {
						// 远程访问获取要备份的数据
						postForEntity = restTemplate.postForEntity(url, "", Map.class);

						body = (Map<String, List<Map<String, Object>>>) postForEntity.getBody().get("data");

						// 返回 datalist 数组
						list = (List<Map<String, Object>>) body.get("dataList");

						// 获取待同步的数据数
						List<Map<String, Object>> count_list = body.get("count");
						Map<String, Object> count_map = count_list.get(0);
						count = (int) count_map.get("count");
						System.out.println("还有"+count+"条数据需要同步");
						
						Map<String, Object> map2 = new HashMap<>();

						map2.put("data", list);
						System.err.println("开始备份+++"+map2);
						// 批量插入
						Map<String, Object> byDataBusinessCode = visuaSqlExampleService
								.getByDataBusinessCode("I" + business_code, jsonService.toJson(map2));
						if (byDataBusinessCode != null && byDataBusinessCode.containsKey(ErrorCodeConstants.HASH_ERR)) {
							System.out.println(byDataBusinessCode.get(ErrorCodeConstants.HASH_ERR).toString());
						} else {
//							若插入成功，则将数据源状态修改为已同步
							System.out.println("插入成功！！！！");
							List<Map<String, Integer>> idList = new ArrayList<>();
//							将被同步的id放入集合
							for (Map<String, Object> data : list) {
								Integer id = (Integer) data.get("id");
								Map<String, Integer> id_map = new HashMap<>();
								id_map.put("id", id);
								// 参数
								idList.add(id_map);

							}
							System.err.println("idList-------"+idList);
//							拼接参数
							Map<String, Object> id_list = new HashMap<>();
							id_list.put("id_list", idList);
							MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
							params.add("params", jsonService.toJson(id_list));

							// 批量更新
							ResponseEntity<String> postForEntitys = restTemplate.postForEntity(
									ip + port + "/common/getData?businessCode=U" + business_code, params, String.class);

							System.out.println("更新成功——————" + postForEntitys.getBody());
						
					}
					
					}

				}

			}


		}

	}
	
	
}
