package com.isoftstone.common.slyt;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface SlytAiDefinition {
	@RequestMapping(value = "/slytAiCaiji", method = RequestMethod.POST)
	void slytAiCaiji();
}
