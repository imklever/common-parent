package com.isoftstone.common.api.service.sms;

public interface ALiSmsService {
	void sendSmsCode(String phone,String code, String signName, String codeTemplate, String accessId, String accessKey);
}
