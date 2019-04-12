package com.isoftstone.common.api.service.sms;

public interface JDMsgService {
	void test(String appId,String publickKey,String id,String accountCode,String userId,String recNumber,String msgContent)throws  Exception ;
}
