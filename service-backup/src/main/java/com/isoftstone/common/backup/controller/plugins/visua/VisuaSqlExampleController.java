package com.isoftstone.common.backup.controller.plugins.visua;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.common.constant.PageConstants;
import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.backup.service.plugins.visua.VisuaSqlExampleVerService;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.VisuaSqlExampleDefinition;
import com.isoftstone.common.plugins.visua.VisuaSqlExampleVer;
import com.isoftstone.common.util.JsonService;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE, path = ServiceBackConstants.PATH_VISUA_SQL)
public class VisuaSqlExampleController implements VisuaSqlExampleDefinition {
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;
	@Autowired
	VisuaSqlExampleVerService visuaSqlExampleVerService;
	@Autowired
	private JsonService jsonService;

	@Override
	public VisuaSqlExample save(@RequestBody String dtoJson) {
		VisuaSqlExample visuaSqlExample = jsonService.parseObject(dtoJson, VisuaSqlExample.class);
		if (StringUtils.isEmpty(visuaSqlExample.getId())) {
			visuaSqlExample.setCreateDate(new Date());
		} else {
			visuaSqlExample.setUpdateDate(new Date());
		}
		 visuaSqlExample = visuaSqlExampleService.save(jsonService.toJson(visuaSqlExample));
		//保存版本信息
		VisuaSqlExampleVer visuaSqlExampleVer =null;
		boolean bl=true;
		visuaSqlExampleVer = visuaSqlExampleVerService.findById(visuaSqlExample.getId());
		VisuaSqlExampleVer newVisuaSqlExampleVer = jsonService.parseObject(dtoJson, VisuaSqlExampleVer.class);		
		if(visuaSqlExampleVer!=null) {
			if(newVisuaSqlExampleVer.getBackupType()!=1) {
			if(visuaSqlExampleVer.getTitle().equals(newVisuaSqlExampleVer.getTitle())
					&&visuaSqlExampleVer.getOutputData().equals(newVisuaSqlExampleVer.getOutputData())
					&&visuaSqlExampleVer.getOutputData().equals(newVisuaSqlExampleVer.getOutputData())
					&&visuaSqlExampleVer.getSqlTemplates().equals(newVisuaSqlExampleVer.getSqlTemplates())) {
				bl=false;
			}
			}
			newVisuaSqlExampleVer.setVersionNum(visuaSqlExampleVer.getVersionNum()+1);
		}else {
			newVisuaSqlExampleVer.setVersionNum(1);
		}
		newVisuaSqlExampleVer.setTimet(new Date());
		newVisuaSqlExampleVer.setId(visuaSqlExample.getId());
		newVisuaSqlExampleVer.setCreateDate(visuaSqlExample.getCreateDate());
		newVisuaSqlExampleVer.setUpdateDate(visuaSqlExample.getUpdateDate());		
		if(bl) {
			visuaSqlExampleVerService.saveVersion(newVisuaSqlExampleVer);	
		}		
		return visuaSqlExample;
	}

	@Override
	public VisuaSqlExample getByBusinessCode(@RequestParam("businessCode") String businesscode) {
		return visuaSqlExampleService.getByBusinessCode(businesscode);
	}

	@Override
	public VisuaSqlExample findById(@RequestParam("id") String id) {
		return visuaSqlExampleService.findById(id);
	}

	@Override
	public Map<String, Object> getByDataBusinessCode(@RequestParam("businessCode") String businesscode,
			@RequestParam("params") String params) {
		return visuaSqlExampleService.getByDataBusinessCode(businesscode, params);
	}

	@Override
	public int getByBusinessCodeCount(@RequestParam("businessCode") String businessCode) {
		return visuaSqlExampleService.getByBusinessCodeCount(businessCode);
	}

	@Override
	public PageInfo<VisuaSqlExample> getByPage(
			@RequestParam(value = "visuaSqlExample", required = false, defaultValue = "{}") String visuaSqlExample,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer pageNo,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "sort", required = false) List<String> sort) {
	
		return visuaSqlExampleService.findByPage(visuaSqlExample,pageNo,pageSize,sort);
	}

	@Override
	public PageInfo<Map<String, Object>> getDataByPage(@RequestParam("businessCode") String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer pageNo,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "sort", required = false) List<String> sort) {
		return visuaSqlExampleService.findBusinessCodeDataByPage(businesscode, params, pageNo, pageSize, sort);
	}

	@Override
	public Map<String, Object> getDataByOneToMany(@RequestParam("businessCode") String businessCode, 
			@RequestParam(value = "params", required = false, defaultValue = "{}")String params) {
		return visuaSqlExampleService.getDataByOneToMany(businessCode,params);
	}

	@Override
	public List<VisuaSqlExample> findByUserId(String userId) {
		return visuaSqlExampleService.findByUserId(userId);
	}

	@Override
	public  PageInfo<Map<String, Object>> getDataByOneToManyByPage(@RequestParam("businessCode")String businessCode,
			@RequestParam("params")String params, @RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort) {
		return visuaSqlExampleService.getDataByOneToManyByPage(businessCode,  params,  page,  size, sort);
	}

	@Override
	public Map<String, Object> testMode(@RequestParam("dtoJson")String dtoJson,@RequestParam("name") String name,@RequestParam("type") String type) {
		return visuaSqlExampleService.testMode(dtoJson,name,type);
	}

}
