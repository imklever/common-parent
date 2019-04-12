package com.isoftstone.common.util.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isoftstone.common.common.CommTreeDto;
import com.isoftstone.common.common.VisuaOutputDto;
import com.isoftstone.common.util.BuildService;
import com.isoftstone.common.util.JsonService;

@Service
public class BuildServiceImpl implements BuildService {
	@Autowired
	JsonService jsonService;

	@Override
	public List<Object> buildTree(List<Map<String, Object>> dataList, String maps, String outPut) {
		List<VisuaOutputDto> visuaOutputDtos = jsonService.parseArray(outPut, VisuaOutputDto.class);

		BeanGenerator generator = new BeanGenerator();
		generator.setSuperclass(CommTreeDto.class);
		Map<String, Class> propertyMap = new HashMap<String, Class>();
		Map<String, Object> row0 = dataList.get(0);
		if (!visuaOutputDtos.isEmpty()) {
			for (VisuaOutputDto visuaOutputDto : visuaOutputDtos) {
				if (row0.containsKey(visuaOutputDto.getName())) {
					propertyMap.put(visuaOutputDto.getName(), row0.get(visuaOutputDto.getName()).getClass());
				} else {
					boolean flag = false;
					for (Map<String, Object> row : dataList) {
						if (row.containsKey(visuaOutputDto.getName())) {
							propertyMap.put(visuaOutputDto.getName(), row.get(visuaOutputDto.getName()).getClass());
							flag = true;
							break;
						}
					}
					if (!flag) {
						propertyMap.put(visuaOutputDto.getName(), String.class);
					}
				}

			}
		}
		BeanGenerator.addProperties(generator, propertyMap);

		List<BeanMap> oldBeanMapDtos = new ArrayList<BeanMap>();
		for (Map<String, Object> map : dataList) {
			BeanMap beanMap = BeanMap.create(generator.create());
			beanMap.putAll(map);
			beanMap.put("content", map);
			oldBeanMapDtos.add(beanMap);
		}
		
		//找到跟节点
		Set<String> pidSet=new HashSet<>();
		Set<String> idSet=new HashSet<>();
		for (BeanMap beanMap : oldBeanMapDtos) {
			pidSet.add((String)beanMap.getOrDefault("pid", "0"));
			idSet.add((String)beanMap.get("id"));
		}
		String pid=null;
		for (String pids : pidSet) {
			if(!idSet.contains(pids)) {
				pid=pids;
				break;
			}
		}
		List<Object> commBeanMapDtos = new ArrayList<Object>();
		for (BeanMap beanMapDto : oldBeanMapDtos) {
			if (beanMapDto.getOrDefault("pid", "0").equals(pid)) {
				commBeanMapDtos.add(beanMapDto);
			}
			for (BeanMap it : oldBeanMapDtos) {
				if (it.getOrDefault("pid", "0").equals(beanMapDto.getOrDefault("id", "0"))) {
					if (beanMapDto.get("children") == null) {
						beanMapDto.put("children", new ArrayList<BeanMap>());
						beanMapDto.put("open", true);
					}
					List<BeanMap> list = (List<BeanMap>) beanMapDto.get("children");
					list.add(it);
				}
			}
		}
		return commBeanMapDtos;
	}

}
