package com.isoftstone.common.backup.service.sms;

public interface ZsyMsgService {
	void test(String appId,String publickKey,String id,String accountCode,String userId,String recNumber,String msgContent)throws  Exception ;
}
