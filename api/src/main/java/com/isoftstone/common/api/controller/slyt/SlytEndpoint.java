package com.isoftstone.common.api.controller.slyt;

import com.isoftstone.common.slyt.SlytServiceClient;
import org.common.constant.ApiMapperUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ApiMapperUrlConstants.SLYTAI)
public class SlytEndpoint {
	@Autowired
	SlytServiceClient slytServiceClient;
	@RequestMapping(value = "/aiCaiJi", method = {
			RequestMethod.POST, RequestMethod.GET })
	public void aiCaiJi(){
		System.out.println("开始采集任务");
		slytServiceClient.slytAiCaiji();
}
}
