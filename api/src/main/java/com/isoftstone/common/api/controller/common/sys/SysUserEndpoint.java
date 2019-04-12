package com.isoftstone.common.api.controller.common.sys;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.api.support.BaseConfig;
import com.isoftstone.common.api.util.RandomValidateCodeUtils;
import com.isoftstone.common.common.sys.SysRoleDto;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.common.sys.hystrix.HystrixSysRoleServiceClient;
import com.isoftstone.common.common.sys.hystrix.HystrixSysUserServiceClient;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.CommUtil;
import com.isoftstone.common.util.JsonService;
import org.apache.commons.codec.digest.DigestUtils;
import org.common.constant.ApiMapperUrlConstants;
import org.common.constant.CommonConstants;
import org.common.constant.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;


@RestController
@RequestMapping(ApiMapperUrlConstants.SYS_USER)
public class SysUserEndpoint {

	@Value("${login.expired-time}")
	long expiredTime = 0L;
	@Value("${cache.type:javaCache}")
	String cacheType;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	LocalCacheFactory localCacheFactory;

	@Autowired
	HystrixSysUserServiceClient hystrixSysUserServiceClient;
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	HystrixSysRoleServiceClient HystrixSysRoleServiceClient;
	
	@Autowired
	JsonService jsonService;
	private HttpServletRequest request;
	private final static long ONE_Day = 1000*60*60*24;
	
//	@RequestMapping(value = "/insertJd", method = { RequestMethod.POST,
//			RequestMethod.GET })
	//@Scheduled(fixedRate=ONE_Day)
	public void insert_Jd_Findbussiness() {
	String param_json="{'userId':170,'customerId':0}";
	
	String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		调用下面的方法获取sign
		String sign=getSign("0CB03DB1F1CBA9F1EC0674155F6AD0AC","D6DD6006FBAC6846B4DD45BB12557E27", "companypackage.getResult",param_json,date);
	
//		String url="http://datastar-south.jdcloud.com/api?method=companypackage.getResult&timestamp="+date+"&sign="+sign+"&access_key=D6DD6006FBAC6846B4DD45BB12557E27&param_json="+param_json;
		
		String url="http://datastar-south.jdcloud.com/api";
		
		System.out.println("url++++"+url);
		System.out.println("/method=companypackage.getResult&timestamp="+date+"&sign="+sign+"&access_key=D6DD6006FBAC6846B4DD45BB12557E27&param_json="+param_json);
		String sendPost = sendPost(url, "method=companypackage.getResult&timestamp="+date+"&sign="+sign+"&access_key=D6DD6006FBAC6846B4DD45BB12557E27&param_json="+param_json);
		System.err.println(sendPost);
		net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(sendPost);
		
		Map<String, List<Map<String, String>>> map = (Map<String, List<Map<String, String>>>)fromObject.get("result");
		System.out.println("map+++++"+map);
		List<Map<String, String>> list = map.get("data");
//		获取数据库中的数据，和接口数据对比，若存在相同venderName，更新，否则新增
		Map<String, String> map_menu=new HashMap<>();
		map_menu.put("menu", "招商管理");
		Map<String, Object> datalist = hystrixVisuaSqlExampleClient.getByDataBusinessCode("S10029", jsonService.toJson(map_menu));
		List<Map<String, String>> list_data = (List<Map<String, String>>) datalist.get("datalist");
		List<String> vender_list=new LinkedList<>();
		for (Map<String, String> map3 : list_data) {
			String venderName = map3.get("venderName");
			vender_list.add(venderName);
		}
		System.out.println("vender_list----"+vender_list);
		for (Map<String, String> map2 : list) {
			
			String area = map2.get("area");
			int indexOf = area.indexOf('-');
			String area_province = area.substring(0, indexOf);
			String	area_downtown=area.substring(indexOf+1);
			map2.put("area_province", area_province);
			map2.put("area_downtown", area_downtown);
			String vender_name=map2.get("venderName");
			if (vender_list.contains(vender_name)) {
			Map<String, Object> datas = hystrixVisuaSqlExampleClient.getByDataBusinessCode("M10029", jsonService.toJson(map2));
			if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
				System.out.println(datas.get(ErrorCodeConstants.HASH_ERR).toString());
					}
			}else {
				Map<String, Object>datas= hystrixVisuaSqlExampleClient.getByDataBusinessCode("I10029",jsonService.toJson(map2));
				if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
					System.out.println(datas.get(ErrorCodeConstants.HASH_ERR).toString());
				}
			}
		}	
		
	}
//	加密得到sign
	public static String getSign(String accessKeySecret,String access_key,String method,String param_json,String date){
		
		String sign=null;
		String sign_org=accessKeySecret+"access_key"+access_key+"method"+method+"param_json"+param_json+"timestamp"+date+accessKeySecret;
		System.out.println("sign_org++++++"+sign_org);
//		commons-codec-1.11包下 org.apache.commons.codec.digest.DigestUtils下的MD5加密
		sign=DigestUtils.md5Hex(sign_org);
		System.out.println("sign+++"+sign);
		return sign;
	}
	
	public static String sendPost(String url, String param) {
		String result = "";
		BufferedReader bufferedReader = null;
		PrintWriter out = null;
		try {
			//1、2、读取并将url转变为URL类对象
			URL realUrl = new URL(url);
			
			//3、打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			//4、设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		
			// 发送POST请求必须设置如下两行  
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			//5、建立实际的连接
			//connection.connect();
			//获取URLConnection对象对应的输出流
			out = new PrintWriter(connection.getOutputStream());
			//发送请求参数
			out.print(param);
			//flush输出流的缓冲
			out.flush();
			//
			
			//6、定义BufferedReader输入流来读取URL的响应内容
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line;
			while(null != (line = bufferedReader.readLine())) {
				result += line;
			}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("发送POST请求出现异常！！！"  + e);
			e.printStackTrace();
		}finally {        //使用finally块来关闭输出流、输入流 
			try {
				if(null != out) {
					out.close();
				}
				if(null != bufferedReader) {
					bufferedReader.close();
				}
			}catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return result;
	}

	
//	京东登录接口
	@RequestMapping(value = "/jdlogin", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object findBy_jdUserPass(
			@RequestParam(value = "token", required = true) String token
		
			) {
		String url="http://cyhz-test.xjoycity.com/ic-passport-web/passport/getUserInfo?token="+token;
		
		String body = restTemplate.getForEntity(url, String.class).getBody();
		Map<String,Object> json=JSONObject.parseObject(body,Map.class);
		//根据url从京东查pin
		String pin = (String) json.get("pin");
		Map<String, Object> pinjson=new HashMap<>();
		pinjson.put("pin", pin);
		System.out.println(pinjson.toString()+"-----json----"+json.toString());
		
		//根据pin去数据库查
		Map<String, Object> datas = hystrixVisuaSqlExampleClient.getByDataBusinessCode("P08003",jsonService.toJson(pinjson));
	System.out.println(datas+"aaaaaaaaaaaaaaaaaaaaa");	
		
		//在自己数据库查不到就手动插入，sql完成
		/*if(dataByBusinessCode==null ||dataByBusinessCode==""){
			Object num = comm.getDataByBusinessCode(request,"P08003", json.toString());
		}*/
		
		SysUserDto sysUserDto =hystrixSysUserServiceClient.findByPhone(pin);
		String type=null;
		doUserCache(sysUserDto,type,null);
		return APIResult.createSuccess(sysUserDto);

		}
			
		
	
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST,
			RequestMethod.GET})

	public Object findByUserPass(
			@RequestParam(value = "username", required = true) String name,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "sid", required = false) String sid,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "unique", required = false) String unique,
			@RequestParam(value = "sys", required = false,defaultValue="0") String sys
			) {
		String errCode ="";
		String errMsg ="用户名或密码错误！";
		if (StringUtils.isEmpty(type) || CommonConstants.PC_TYPE.equals(type)) {
			// 处理验证码
			Object randomCode = BaseConfig.VALIDATE_CODE_HASH_MAP
					.get("valid_code_" + sid);
			
			
			if(randomCode==null){
				return APIResult.createInstance("验证码不正确,请重新输入!");
			}else {
				Map<String, Object> codeMap = (HashMap<String, Object>)randomCode;
				Object random_code =codeMap.get("random");
				if (random_code == null) {
					return APIResult.createInstance("验证码不正确,请重新输入!");
				}
				if (!random_code.toString().equalsIgnoreCase(code)) {
					return APIResult.createInstance("验证码不正确,请重新输入!");
				}
			}
 
			BaseConfig.VALIDATE_CODE_HASH_MAP.remove("valid_code_" + sid);
		}

		
		
		
		//手机号登录
/*		SysUserDto sysUserDto =hystrixSysUserServiceClient.findByPhone(name);
		if(sysUserDto==null)
		{
			password = CommUtil.getMD5(name + password).toLowerCase();
		    sysUserDto = hystrixSysUserServiceClient.findByUserPass(
					name, password);
		}
		else {
			String userName = sysUserDto.getUsername();
			String userPassword =sysUserDto.getPassword();
			password = CommUtil.getMD5(userName+password).toLowerCase();
			if(!password.equals(userPassword))
			{
				sysUserDto = null;
			}
		}*/
		SysUserDto sysUserDto =hystrixSysUserServiceClient.findByPhone(name);
		if(sysUserDto==null)
		{
			/*password = CommUtil.getMD5(name + password).toLowerCase();
		    sysUserDto = hystrixSysUserServiceClient.findByUserPass(
					name, password);*/
			errCode="err_001";
		}
		else {
			String userName = sysUserDto.getUsername();
			String userPassword =sysUserDto.getPassword();
			password = CommUtil.getMD5(userName+password).toLowerCase();
			if(!password.equals(userPassword))
			{
				sysUserDto = null;
				errCode="err_002";			
			}
		}
		
		doUserCache(sysUserDto,type,unique);
		
		if(sysUserDto==null)
		{			
			Map<String, Object> errMap=new HashMap<String, Object>();
			errMap.put("sys_type", sys);
			errMap.put("err_code", errCode);
			
			Map<String, Object> visuaSqlExampleMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("E06003", jsonService.toJson(errMap));
			if(visuaSqlExampleMap.containsKey("select")){
				List<Map<String, Object>> vseList = (List<Map<String, Object>>)visuaSqlExampleMap.get("select");
				 
				for (Map<String, Object> map : vseList) {
					errMsg =map.get("name").toString();
				}
			}
						
			return APIResult.createInstance(errMsg);
		}
		else {
			return APIResult.createSuccess(sysUserDto);
		}
	
	}

	@RequestMapping(value = "/findById", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object findById(
			@RequestParam(value = "id", required = true) String id) {
		SysUserDto sysUserDto = hystrixSysUserServiceClient.findById(id);
		return APIResult.createSuccess(sysUserDto);
	}

	@RequestMapping(value = "/getVerifyData", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object getVerifyData(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", request.getSession().getId());
		map.put("imageUrl", ApiMapperUrlConstants.SYS_USER + "/getVerifyCode");

		Object object = jsonService.toJson(map);

		return APIResult.createSuccess(object);
	}

	@RequestMapping(value = "/getVerifyCode", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getVerifyCode(
			@RequestParam(value = "sid", required = true) String sid,
			HttpServletResponse response) {
		try {
			response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
			response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0);
			RandomValidateCodeUtils randomValidateCode = new RandomValidateCodeUtils();
			randomValidateCode.getRandcode(sid,expiredTime, response);// 输出验证码图片方法
		} catch (Exception e) {

		}
	}

	@RequestMapping(value = "/updatePasswordById", method = {
			RequestMethod.POST, RequestMethod.GET })
	public Object updatePasswordById(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "newPassword", required = true) String newPassword) {

		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("token");
		}

		CacheService cacheService = localCacheFactory
				.getCacheService(cacheType);
		SysUserDto currentUser = (SysUserDto) cacheService.get(CommonConstants.CACHE_TYPE_KEY_USER, token);

		String userName = currentUser.getUsername();
		String oldPassword = currentUser.getPassword();
		password = CommUtil.getMD5(userName + password);

		if (!password.equals(oldPassword)) {
			return APIResult.createInstance("原密码不正确。请重新输入！");
		}

		newPassword = CommUtil.getMD5(userName + newPassword);
		SysUserDto sysUserDto = new SysUserDto();
		sysUserDto.setId(id);
		sysUserDto.setPassword(newPassword);
		int successCount = hystrixSysUserServiceClient
				.updateByPrimaryKeySelective(sysUserDto);
		return APIResult.createSuccess(successCount);
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object logout(
			@RequestParam(name = "userId", required = true) String userId,
			@RequestParam(name = "token", required = false) String token) {
		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("token");
		}

		CacheService cacheService = localCacheFactory
				.getCacheService(cacheType);

		cacheService.delUserToken(CommonConstants.CACHE_TYPE_KEY_USER, token, userId);
		return APIResult.createSuccess();
	}
	
	
	@RequestMapping(value = "/forgetPassword", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object forgetPassword(
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "type", required = false) String type) {
 
		//手机号登录
		SysUserDto sysUserDto =hystrixSysUserServiceClient.findByPhone(phone);
		
		doUserCache(sysUserDto,type,"");
		 
		return APIResult.createSuccess(sysUserDto);
	}
	
	/**
	 * 新增用户缓存
	 * @param sysUserDto
	 */
	private void doUserCache(SysUserDto sysUserDto,String type,String unique){
		if (sysUserDto != null) {
			String token = sysUserDto.getToken();
			CacheService cacheService = localCacheFactory.getCacheService(cacheType);
			
			//SysUserDto  oldsysUserDto=(SysUserDto) 
			    //cacheService.get(CommonConstants.CACHE_TYPE_KEY_USER, token);
			
			String userId = sysUserDto.getId();
		    if(StringUtils.isEmpty(unique)) {
		         // 生成token
		        token = CommUtil.generateToken();
		    }
		    Long duetime = new Date().getTime() + expiredTime * 60 * 1000;
            String overduetime = String.valueOf(duetime);
            // 登录成功 更新过期日期 、token值
            hystrixSysUserServiceClient.updateTokenById(userId, token,overduetime);
            sysUserDto.setToken(token);
            sysUserDto.setOverduetime(overduetime);
            cacheService.delUserToken(CommonConstants.CACHE_TYPE_KEY_USER, token, userId);
			// 用户
			cacheService.put(CommonConstants.CACHE_TYPE_KEY_USER, token, sysUserDto);
			cacheService.put(CommonConstants.CACHE_TYPE_KEY_USER, userId, "user_" + token);
			
			Set<String> busSet = new HashSet<>();
			
			// 处理京东需求：根据用户门店设备获取菜单的权限 
			try {
				if(!StringUtils.isEmpty(type)&& "jd".equals(type)){
					Map<String, Object>obj=new HashMap<String, Object>();
					//"{userId:"+userId+"}"
					obj.put("userId", sysUserDto.getId());
					Map<String, Object> visuaSqlExampleMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("E06002", jsonService.toJson(obj));
					if(visuaSqlExampleMap.containsKey("select")){
						List<Map<String, Object>> vseList = (List<Map<String, Object>>)visuaSqlExampleMap.get("select");
						for (Map<String, Object> map : vseList) {
							busSet.add(map.get("business_code").toString());
						}
					}
				}else {
					// 权限
					List<VisuaSqlExample> visuaSqlExampleList = hystrixVisuaSqlExampleClient.findByUserId(userId);
					for (VisuaSqlExample visuaSqlExample : visuaSqlExampleList) {
						busSet.add(visuaSqlExample.getBusinessCode());
					}
				}
			} catch (Exception e) {
				 e.printStackTrace();
			}
			 
			cacheService.put(CommonConstants.CACHE_TYPE_KEY_PERMISSION, token, busSet);
			
			// 角色
			List<SysRoleDto> sysRoleList = HystrixSysRoleServiceClient
					.findByUserId(userId);
			cacheService.put(CommonConstants.CACHE_TYPE_KEY_ROLE, token, sysRoleList);
		}
	}

}
