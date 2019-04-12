package com.isoftstone.common.networm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.isoftstone.common.util.JsonService;


public class EnterpriseDemo { 
	@Autowired
	JsonService jsonService;
	public static void main(String[] args) {
		int pageIndex = 1;
		while(true){
			String body = getBody(pageIndex);
			if(body==null || StringUtils.isEmpty(body)||body=="null"){
			   break;
			}
		    System.out.println("body: "+body);

		    try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     pageIndex++;
		}
	}
	
	public static String getBody(int pageIndex){
		 		
		RestTemplate restTemplate=new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page="+pageIndex+"&num=80&sort=symbol&asc=1&node=sw_tx&symbol=&_s_r_a=page", String.class);
		return responseEntity.getBody();
	}

}
