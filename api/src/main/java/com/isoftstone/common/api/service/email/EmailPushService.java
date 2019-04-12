package com.isoftstone.common.api.service.email;

public interface EmailPushService {
	public void sendEmail(String SMTPSERVER,String SMTPPORT,String ACCOUT,String PWD,String mailTheme,String mailText,String toemail) throws Exception;
}
