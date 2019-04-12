package com.isoftstone.common.backup.service.sms;

public interface ALiSmsService {
	void sendSmsCode(String phone,String code, String signName, String codeTemplate, String accessId, String accessKey);
}
