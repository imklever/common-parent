package com.isoftstone.common.backup.controller.slyt;

import java.util.HashMap;

import org.common.constant.ServiceBackConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isoftstone.common.backup.service.SlytAiService;
import com.isoftstone.common.slyt.SlytAiDefinition;

@RefreshScope
@RestController
@RequestMapping(name = ServiceBackConstants.BACK_SERVICE)
public class SlytAiController  implements SlytAiDefinition{

	@Autowired
	SlytAiService slytAiService;

	@Override
	public void slytAiCaiji() {
		slytAiService.slytAiCaiji(new HashMap<String,Object>());
	}
	
}