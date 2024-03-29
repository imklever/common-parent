package com.isoftstone.common.mongobackup;

import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.util.JsonService;
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdMsgTest {
	@Autowired
	RestTemplate  restTemplate;
	@Autowired
	JsonService jsonService;
	@Test
	public void test() throws  Exception {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		
		 String appId = "comma1961247c6664872aa15ccd401742d34";//TODO 需要修改
	     String publickKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ0ZcPWGPglsntnSn1CVJh8pLJGopKGfZur4jNsBkt3zKDlOWJf5B1vIO7I8ucajL1UeWcjfymQlMXmtU46wfRR6dZ3kPkfeNzSAjD2THHCrnd5mkD/X1cbITNkJpFAFV3Hmxoc4njREO1A1ccwpNY/ZbuLxSWolJ/LmGJeeOBaQIDAQAB";        
	     HttpHeaders headers = new HttpHeaders();
	     
	     HashMap<String, Object> header = new HashMap<String, Object>();
	      String serviceName = "smsMulti";
	      header.put("serviceName", serviceName);
	      header.put("version", "1.0");
	      header.put("clientMessageId", serviceName + UUID.randomUUID().toString().replace("-", ""));
	      header.put("timestamp", System.currentTimeMillis());
	      
	      HashMap<String, Object> body = new HashMap<String, Object>();
	      body.put("clientMessageId", serviceName + UUID.randomUUID().toString().replace("-", ""));//TODO 需要修改
	      body.put("accountCode", "1000092330");//TODO 需要修改
	      body.put("userId", "babid");//TODO 需要修改
	      body.put("recNumber", "17600529050");//TODO 需要修改
	      body.put("msgContent", "短信测试");//TODO 需要修改
	        
	      HashMap<String, Object> map = new HashMap<String, Object>();
	      map.put("header", header);
	      map.put("body", body);

	      byte[] encodeDate = RSAUtils.encryptByPublicKey(
	    		  jsonService.toJson(map)
	    		  .getBytes("utf-8"), publickKey);
	     String bizParam = Base64Utils.encode(encodeDate);
      //签名校验
       String sign = Md5Utils.md5(jsonService.toJson(map));
        
        requestEntity.add("appId", appId);
        requestEntity.add("sign", sign);
        requestEntity.add("bizParam", bizParam);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestEntity, headers);

        String url="https://commcso.jd.com/open/api/invoke.do";
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getBody());
	}
}
