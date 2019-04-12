package com.isoftstone.common.api.service.sms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.isoftstone.common.api.service.sms.ALiSmsService;
import com.isoftstone.common.util.ErrorCode;
import com.isoftstone.common.util.ServiceException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ALiSmsServiceImpl implements ALiSmsService {
/*	public static void main(String[] args) {
		SmsServiceImpl smsServiceImpl=new SmsServiceImpl();
		smsServiceImpl.sendSmsCode("15635971615", "{\"code\":\"111222\"}","第一防护","SMS_139982697","LTAIiTR3n2IfX11q","ds3fAElUXasG7Fq8gn8e9XXh2UtROx");
	}*/

	@Override
	public void sendSmsCode(String phone,String code, String signName, String codeTemplate, String accessId, String accessKey) {
		if (!phone.matches("^1[3|4|5|7|8][0-9]{9}$")) {
			throw new ServiceException("订单", ErrorCode.COMMON);
		}
		// 判断用户输入的电话号码是否频繁发送
		/*
		 * if (isSendOfen(phone)) { return; }
		 */
		/*JSONObject smsJson=new JSONObject();
        smsJson.put("code",code);*/
        SendSmsResponse sendSmsResponse=null;
        try {
            sendSmsResponse = send(phone,signName,codeTemplate,code,accessId, accessKey);
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("短信验证码发送失败");
            return;
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //短信发送成功，将短信记录到redis中
           //redisCode(sms);
            System.out.println("短信发送成功");
        }

	}

	private SendSmsResponse send(String phone, String signName, String templateCode, String params, String accessId, String accessKey) throws ClientException {
		String product="Dysmsapi";
		String domain="dysmsapi.aliyuncs.com";
		 //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessId,
                accessKey);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(params);
        request.setOutId(UUID.randomUUID().toString());
        //请求失败这里会抛ClientException异常
        return acsClient.getAcsResponse(request);

	}

	// 判断验证功发送时候频繁
	/*
	 * private boolean isSendOfen(String phone) { if(redisService.get(phone)==null)
	 * { return false; }else{ //判断上一次记录的时间和当前时间进行对比，如果两次相隔时间小于120s，视为短信发送频繁 Sms
	 * sms=redisService.get(phone,Sms.class); //两次发送短信中间至少有2分钟的间隔时间
	 * if(sms.getTime()+120*1000>=System.currentTimeMillis()) { return true; }
	 * return false; } }
	 */

}
