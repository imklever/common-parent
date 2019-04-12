package com.isoftstone.common.api.controller.ali.pay;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.isoftstone.common.api.domain.comon.AlipayConfig;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
@RestController
@RequestMapping("/payali")
public class AliPayEndpoint {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	JsonService jsonService;
	@RequestMapping("/prepay")
	public Object prepay(HttpServletResponse response,
			String out_trade_no) throws IOException, AlipayApiException {
		 Map map=getSendOrder(out_trade_no);
		 Map<String, Object>remap=new HashMap<>();
		if(map==null) {
			return  APIResult.createInstance();
		}else {
				AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
	    		AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, 
	    		AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
			    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
			    alipayRequest.setReturnUrl(AlipayConfig.return_url);
			    alipayRequest.setNotifyUrl(AlipayConfig.notify_url);//在公共参数中设置回跳和通知地址
			    Map<String,Object>sle=new HashMap<>(); 
			    sle.put("out_trade_no", map.get("send_order_id"));
			    sle.put("total_amount", map.get("amount"));
			    sle.put("subject", map.get("subject"));
			    sle.put("product_code","QUICK_WAP_WAY");
			    alipayRequest.setBizContent(JSONObject.toJSONString(sle));//填充业务参数
			    String form = alipayClient.pageExecute(alipayRequest,"GET").getBody(); //调用SDK生成表单
			    remap.put("url", form);
		}
		return APIResult.createSuccess(remap);
		
	}
	private Map getSendOrder(String out_trade_no) {
		System.out.println(out_trade_no);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("order_id", out_trade_no);
		map.add("paytype", "1");
		Map re=null;
		HttpEntity<MultiValueMap<String, String>> request = 
				new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url="http://tour.v3ws.com:82/index.php/api/User/setOrderNo";
		ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
		Map<String,Object> json=JSONObject.parseObject(response.getBody(),Map.class);
		if(json.containsKey("code")&&(int)json.get("code")==0) {
			if(json.containsKey("data")) {
				re=(Map<String, Object>) json.get("data");
				System.out.println(re);
			}
		}
		System.out.println(response.getBody());
		return re;
	}
	@RequestMapping("/prepaybybusinessCode")
	public Object prepayD(HttpServletResponse respons,@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params)  {
		Map<String, Object> datas = hystrixVisuaSqlExampleClient
				.getByDataBusinessCode(businesscode, params);
		System.out.println(jsonService.toJson(datas));
		if(datas.containsKey("confgList")&&datas.get("confgList")!=null) {
			  List<Map<String, Object>> rows=(List<Map<String, Object>>) datas.get("confgList"); 
			  AlipayClient alipayClient=null;
			  AlipayTradeWapPayRequest alipayRequest=null;
			  if(rows!=null&&rows.size()==1) {
				   Map<String, Object> row=rows.get(0);
				   String url=row.get("getway").toString();
				   String appid=row.get("appid").toString();
				   String alipay_pulic_key=row.get("alipay_pulic_key").toString();
				   String format=row.get("format").toString();
				   String charset=row.get("charset").toString();
				   String rsa_priate_key=row.get("rsa_priate_key").toString();
				   String signtype=row.get("signtype").toString();
				   String notify_url=row.get("notify_url").toString();
				   String return_url=row.get("return_url").toString();
				   alipayClient = new DefaultAlipayClient(url, appid,
							rsa_priate_key,format, charset, 
							alipay_pulic_key,signtype);
				   alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
				    alipayRequest.setReturnUrl(return_url);
				    alipayRequest.setNotifyUrl(notify_url);//在公共参数中设置回跳和通知地址
			  }else {
				  APIResult.createInstance("未阿里支付信息");  
			  }
			  if(datas.containsKey("oder_list")&&datas.get("oder_list")!=null) {
					 List<Map<String, Object>> rowsoder=(List<Map<String, Object>>) datas.get("oder_list"); 
					  if(rows!=null&&rows.size()==1) {
						  Map<String, Object> row=rowsoder.get(0);
						  double pr=(double) row.get("price");
						  String product_code=row.get("product_code").toString();
						  String send_mer_order_id=row.get("send_mer_order_id").toString();
						  String subject=row.get("subject").toString();
						  Map<String,Object>sle=new HashMap<>(); 
						    sle.put("out_trade_no", send_mer_order_id);
						    sle.put("total_amount", pr);
						    sle.put("subject", subject);
						    sle.put("product_code",product_code);
						    alipayRequest.setBizContent(JSONObject.toJSONString(sle));//填充业务参数
						    try { 
						    	Map<String, Object>remap=new HashMap<>();
								String form = alipayClient.pageExecute(alipayRequest,"GET").getBody();
								remap.put("url", form);
								return APIResult.createSuccess(remap);
							} catch (AlipayApiException e) {
								e.printStackTrace();
							} //调用SDK生成表单
					  }
			  }else {
				  APIResult.createInstance("订单状态错误 !");  
			  }
			
		}
		return APIResult.createInstance("");
	}
	private Map getnewSendOrder(String out_trade_no) {
		System.out.println(out_trade_no);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("order_id", out_trade_no);
		map.add("paytype", "1");
		Map re=null;
		HttpEntity<MultiValueMap<String, String>> request = 
				new HttpEntity<MultiValueMap<String, String>>(map, headers);
		String url="http://tour.v3ws.com:82/index.php/api/User/setOrderNo";
		ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
		Map<String,Object> json=JSONObject.parseObject(response.getBody(),Map.class);
		if(json.containsKey("code")&&(int)json.get("code")==0) {
			if(json.containsKey("data")) {
				re=(Map<String, Object>) json.get("data");
				System.out.println(re);
			}
		}
		System.out.println(response.getBody());
		return re;
	}
}
