package com.isoftstone.common.api.controller.plugins.visua;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.common.constant.ApiMapperUrlConstants;
import org.common.constant.ErrorCodeConstants;
import org.common.constant.PageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.PageInfo;
import com.isoftstone.common.api.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;

@RestController
@RequestMapping(ApiMapperUrlConstants.VISUA_SQL)
public class VisuaSqlExampleEndpoint {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;

	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public Object save(@RequestParam(value = "params", required = true) String dtoJson) {
		VisuaSqlExample result = hystrixVisuaSqlExampleClient.save(dtoJson);
		return APIResult.createSuccess(result);
	}

	@RequestMapping(value = "/getByBusinessCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Object getByBusinessCode(@RequestParam(value = "businessCode", required = true) String businessCode) {
		VisuaSqlExample result = hystrixVisuaSqlExampleClient.getByBusinessCode(businessCode);
		return APIResult.createSuccess(result);
	}

	@RequestMapping(value = "/getByPage", method = { RequestMethod.POST, RequestMethod.GET })
	public Object getByPage(@RequestParam(value = "params", required = false, defaultValue = "{}") String visuaSqlExample,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort) {
		PageInfo<VisuaSqlExample> visuaSqlExamples = hystrixVisuaSqlExampleClient.getByPage(visuaSqlExample, page, size,
				sort);
		return APIResult.createSuccess(visuaSqlExamples);
	}

	@RequestMapping(value = "/getByBusinessCodeCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Object getByBusinessCodeCount(@RequestParam(value = "businessCode", required = true) String businessCode) {
		int count = hystrixVisuaSqlExampleClient.getByBusinessCodeCount(businessCode);
		return APIResult.createSuccess(count);
	}
	
	@RequestMapping(value="/testMode", method={RequestMethod.POST,RequestMethod.GET} )
    public Object testMode(HttpServletRequest request,
    		@RequestParam(value = "params",required = true) String dtoJson,
    			@RequestParam(value = "name",required = true) String name,
    			@RequestParam(value = "type",required = true) String type){
		Map<String, Object> datas= hystrixVisuaSqlExampleClient.testMode(dtoJson,name,type);
			if(datas!=null&&datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
				return APIResult.createInstance(
							datas.get(ErrorCodeConstants.HASH_ERR)
							.toString());
			}else if(datas!=null&&datas.containsKey(name)) {
				return APIResult.createSuccess(datas.get(name));
			}else {
				return APIResult.createInstance();
			}
    }

	/*
	 * @RequestMapping(value = "/getById", method = { RequestMethod.POST,
	 * RequestMethod.GET }) public Object getById(@RequestParam(value = "id",
	 * required = true) String id) { VisuaSqlExample result =
	 * hystrixVisuaSqlExampleClient.findById(id); return
	 * APIResult.createSuccess(result); }
	 */

}
