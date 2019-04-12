package com.isoftstone.common.api.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.PageInfo;
import com.isoftstone.common.api.service.common.FileUploadHandle;
import com.isoftstone.common.api.service.email.EmailPushService;
import com.isoftstone.common.api.service.oss.OssStorageFactory;
import com.isoftstone.common.api.service.plugins.visua.VisuaSqlExampleService;
import com.isoftstone.common.api.service.sms.ALiSmsService;
import com.isoftstone.common.api.service.sms.JDMsgService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.api.util.ArgsValidateUtils;
import com.isoftstone.common.api.util.RequestParamsUtils;
import com.isoftstone.common.api.util.Video2ImgUtils;
import com.isoftstone.common.common.hystrix.HystrixSysHttpLogServiceClient;
import com.isoftstone.common.common.sys.hystrix.HystrixSysTaskServiceClient;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.BuildService;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.MyProps;
import org.apache.commons.io.FileUtils;
import org.common.constant.ApiMapperUrlConstants;
import org.common.constant.CommonConstants;
import org.common.constant.ErrorCodeConstants;
import org.common.constant.PageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(ApiMapperUrlConstants.COMMON)
public class CommEndpoint {
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	HystrixSysHttpLogServiceClient hystrixSysHttpLogServiceClient;
	@Autowired
	BuildService buildService;
/*	@Autowired
	DrawLineService drawLineService;*/
	@Autowired
	ALiSmsService smsService;
	@Autowired
	EmailPushService emailPushService;
	@Autowired
	RequestParamsUtils RequestParamsUtils;
	@Autowired
	JDMsgService jDMsgService;
	@Autowired
	JsonService jsonService;
	@Autowired
	VisuaSqlExampleService visuaSqlExampleService;
	@Autowired
	HystrixSysTaskServiceClient hystrixSysTaskServiceClient;
	@Autowired
    MyProps mapProps;
	@Autowired
	OssStorageFactory ossStorageFactory;
	@Autowired
	Video2ImgUtils video2img;
	@Autowired
	FileUploadHandle fileUploadHandle;
	
	@RequestMapping(value = "/getBusinessCode", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object getByBusinessCode(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode) {

		VisuaSqlExample result = hystrixVisuaSqlExampleClient
				.getByBusinessCode(businesscode);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inputData", result.getInputData());
		map.put("outputData", result.getOutputData());
		return APIResult.createSuccess(map);
	}

	@RequestMapping(value = "/getData", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object getDataByBusinessCode(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "todo", required = false) String todo,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
		//验证参数是否符合标准
		String validateResult = validate(businesscode, params);
		
		if (!StringUtils.isEmpty(validateResult)) {
			return APIResult.createInstance(validateResult);
		}

		params = RequestParamsUtils.rebuildParam(request, params);
		
		 Map<String, Object> datas=null;
	     if(CommonConstants.TASK_ACTION_START.equals(todo)) {
	            datas= hystrixSysTaskServiceClient.startJob(params);
	     }else if(CommonConstants.TASK_ACTION_STOP.equals(todo)) {
	            datas= hystrixSysTaskServiceClient.stopJob(params);
	     }else if(CommonConstants.TASK_ACTION_RESTART.equals(todo)) {
	            datas= hystrixSysTaskServiceClient.restartJob(params);
	     }else if(CommonConstants.TASK_ACTION_RESCHEDULE.equals(todo)) {
	            datas= hystrixSysTaskServiceClient.rescheduleJob(params);
	     }else if(CommonConstants.TASK_ACTION_DEL.equals(todo)) {
	            datas= hystrixSysTaskServiceClient.delJob(params);
	     }else {
	          datas = getByDataBusinessCode(businesscode, params);
	     }
		if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
			return APIResult.createInstance(datas.get(
					ErrorCodeConstants.HASH_ERR).toString());
		}
		return APIResult.createSuccess(datas);
	}
	@RequestMapping(value = "/getTask", method = { RequestMethod.POST, RequestMethod.GET })
	public Object getTask(
            HttpServletRequest request,
            @RequestParam(value = "businessCode", required = true) String businesscode,
            
            @RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
        params = RequestParamsUtils.rebuildParam(request, params);
       
        return APIResult.createSuccess();
    }
	// 短信验证码发送
	@RequestMapping(value = "/sendMessage", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object sendMessage(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) throws Exception {
		String phone = null;
		String code = null;
		String signName = null;
		String codeTemplate = null;
		String accessId = null;
		String accessKey = null;
		
		String appId = null;
		String publickKey =null;
		String id = null;
		String accountCode = null;
		String userId = null;
		
		Map<String, Object> datas = getByDataBusinessCode(businesscode, params);
		if (datas != null && datas.containsKey("dataList")) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) datas
					.get("dataList");
			for (Map<String, Object> map2 : list) {
				phone = (String) map2.get("phone");
				code = (String) map2.get("content");
				signName = (String) map2.get("signName");
				codeTemplate = (String) map2.get("codeTemplate");
				accessId = (String) map2.get("AccessKeyID");
				accessKey = (String) map2.get("AccessKeySecret");
				
				appId = (String) map2.get("appId");
				publickKey = (String) map2.get("publickKey");
				id = (String) map2.get("id");
				accountCode = (String) map2.get("accountCode");
				userId = (String) map2.get("userId");
			}
			if(accessId!=null&&accessKey!=null) {
				smsService.sendSmsCode(phone, code, signName, codeTemplate,
						accessId, accessKey);
			}else {
				jDMsgService.test(appId, publickKey, id, accountCode, userId, phone, code);
			}
			
		} else {
			return APIResult.createInstance("未配置短信供应商信息！");
		}
		return APIResult.createSuccess();
	}

	// 邮件发送
	@RequestMapping(value = "/sendEmail", method = {
			RequestMethod.POST, RequestMethod.GET })
	public boolean sendEmail(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params)
			throws Exception {
		String SMTPSERVER = null;
		String SMTPPORT = null;
		String ACCOUT = null;// 发件箱地址
		String PWD = null;// 授权码
		String mailTheme = null;// 邮件主题
		String mailText = null;// 邮件内容
		String toemail = null;// 收件箱
		Map<String, Object> map = getByDataBusinessCode(businesscode, "{}");
		if (map != null && map.containsKey("dataList")) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) map
					.get("dataList");
			for (Map<String, Object> map2 : list) {
				SMTPSERVER = (String) map2.get("SMTPSERVER");
				SMTPPORT = (String) map2.get("SMTPPORT");
				ACCOUT = (String) map2.get("ACCOUT");
				PWD = (String) map2.get("PWD");
				mailTheme = (String) map2.get("mailTheme");
				mailText = (String) map2.get("mailText");
				toemail = (String) map2.get("toemail");
			}
		}
		emailPushService.sendEmail(SMTPSERVER, SMTPPORT, ACCOUT, PWD,
				mailTheme, mailText, toemail);
		return true;
	}
	 /*//根据经纬度信息画线
		@RequestMapping(value = "/drawLine", method = { RequestMethod.POST,
				RequestMethod.GET })
		public Object drawLine(
				HttpServletRequest request,
				@RequestParam(value = "businessCode", required = true) String businesscode,
				@RequestParam(value = "params", required = false, defaultValue = "{}") String params) throws Exception {
			Map<String, Object> datas = getByDataBusinessCode(businesscode, params);
			if (datas != null && datas.containsKey("dataList")) {
				List<Map<String, Object>> pointList = (List<Map<String, Object>>) datas.get("dataList");
				drawLineService.drawLine(pointList);
			} else {
				return APIResult.createInstance("轨迹信息获取失败！");
			}
			return APIResult.createSuccess();
		}*/
	@RequestMapping(value = "/getDataByPage", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object getDataByBusinessCodeByPage(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort) {
		params = RequestParamsUtils.rebuildParam(request, params);
		PageInfo<Map<String, Object>> datas = hystrixVisuaSqlExampleClient
				.getDataByPage(businesscode, params, page, size, sort);
		return APIResult.createSuccess(datas);
	}
	@RequestMapping(value = "/getDataByOneToManyByPage", method = {
			RequestMethod.POST, RequestMethod.GET })
	public Object getDataByOneToManyByPage(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params,
			@RequestParam(value = "page", required = false, defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PageConstants.DEFAULT_PAGE_SIZE) Integer size,
			@RequestParam(value = "sort", required = false) List<String> sort) {
		params = RequestParamsUtils.rebuildParam(request, params);
		PageInfo<Map<String, Object>> datas = hystrixVisuaSqlExampleClient
				.getDataByOneToManyByPage(businesscode, params, page, size, sort);
		return APIResult.createSuccess(datas);
	}

	@RequestMapping(value = "/getDataByOneToMany", method = {
			RequestMethod.POST, RequestMethod.GET })
	public Object getDataByOneToMany(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
		params = RequestParamsUtils.rebuildParam(request, params);
		Map<String, Object> datas = hystrixVisuaSqlExampleClient
				.getDataByOneToMany(businesscode, params);
		if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
			return APIResult.createInstance(datas.get(
					ErrorCodeConstants.HASH_ERR).toString());
		}
		return APIResult.createSuccess(datas);
	}

	@RequestMapping(value = "/getDataByTree", method = { RequestMethod.POST,
			RequestMethod.GET })
	public Object getDataByBusinessCodeByTree(
			HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params,
			@RequestParam(value = "maps", required = false, defaultValue = "{}") String maps) {
		params = RequestParamsUtils.rebuildParam(request, params);
		Map<String, Object> datas = getByDataBusinessCode(businesscode, params);
		Map<String, Object> treeDates = new HashMap<String, Object>();
		if (datas != null) {
			if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
				return APIResult.createInstance(datas.get(
						ErrorCodeConstants.HASH_ERR).toString());
			}
			for (String key : datas.keySet()) {
				if (CommonConstants.VISUA_OUTPUT.equals(key)
						|| CommonConstants.VISUA_INPUT.equals(key)
						|| CommonConstants.SUPPORT_TO_DO.equals(key)
						|| CommonConstants.UDF_TO_DO.equals(key)) {
					treeDates.put(key, datas.get(key));
				} else {
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) datas
							.get(key);
					String output = (String) datas
							.get(CommonConstants.VISUA_OUTPUT);
					List<Object> lia = buildService.buildTree(list, maps,
							output);
					treeDates.put(key, lia);
				}
			}
		}
		return APIResult.createSuccess(treeDates);
	}

	private Map<String, Object> getByDataBusinessCode(String businesscode,
			String params) {
		Map<String, Object> datas = hystrixVisuaSqlExampleClient
				.getByDataBusinessCode(businesscode, params);
		/*if (datas == null) {
			for (int i = 0; i < 5; i++) {
				datas = hystrixVisuaSqlExampleClient.getByDataBusinessCode(
						businesscode, params);
				if (datas != null) {
					return datas;
				}
			}
		} else {
			return datas;
		}*/
		return datas;
	}

	private String validate(String businesscode, String params) {
		String errorMessage = "";
		java.util.Map<String, Object> paramMap = jsonService.parseMap(params);
		VisuaSqlExample result = hystrixVisuaSqlExampleClient
				.getByBusinessCode(businesscode);
		String inputData = result.getInputData();
		List<JSONObject> list = jsonService.parseArray(inputData, JSONObject.class);
		for (JSONObject obj : list) {
			JSONObject map = obj;
			if (!StringUtils.isEmpty(map.get("name"))) {
				Object objValue = paramMap.get(map.get("name"));
				String displayName = map.get("value").toString();
				JSONObject verifyTypeMap =  (JSONObject) map.get("verifyType");
				String messageStr = "";
			   if(verifyTypeMap!=null) {
					for (Entry<String, Object> entry : verifyTypeMap.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						messageStr += ArgsValidateUtils.validateResult(key, value, objValue);
					}
			   }
			   if (!StringUtils.isEmpty(messageStr)) {
					messageStr = messageStr.substring(0,
							messageStr.length() - 1);
					errorMessage += displayName + messageStr;
				}
			}
		}

		return errorMessage;
	}
	/*
	 * Excel导出功能
	 */
	@RequestMapping(value="/getExcelByBusinessCode", method={RequestMethod.POST,RequestMethod.GET} )
	public Object getExcelByBusinessCodePage(HttpServletRequest request,
		 	@RequestParam(value = "businessCode",required = true) String businesscode,
 			@RequestParam(value = "params", required = false, defaultValue = "{}") String params,
			@RequestParam(value = "sort", required = false) List<String> sort,
			HttpServletResponse res){
		    Map<String, Object>map=new HashMap<String, Object>();
		    map.put("meeting_id", "d750eded-25f6-11e9-9389-fa163e347166");
		    
		    System.out.println(jsonService.toJson(map)+"aaaaaaaaaaa");
		    System.err.println(businesscode+"----+++----"+params);
		     VisuaSqlExample result = hystrixVisuaSqlExampleClient.getByBusinessCode(businesscode);
//	   		params=systemParamsService.buildAddSystem(request,params);
		     visuaSqlExampleService.exportExcel(res,params,result,sort);
		    return null;
  
}
	//多文件上传
    @ResponseBody
    @RequestMapping(value = "/sys/upload", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public Object handleFileUpload(HttpServletRequest request) {
    	String storeId = request.getParameter("storeId");
    	String upType = request.getParameter("upType");
    	String fId = request.getParameter("fId");
    	String businessCode = request.getParameter("businessCode");
      	String selbusinessCode = request.getParameter("selBusinessCode");
    	String upbusinessCode = request.getParameter("upBusinessCode");
    	String ossKey = request.getParameter("ossKey");
    	String substep = request.getParameter("substep");
    	String item = request.getParameter("item");
    	String tableName = request.getParameter("tableName");
    	String rootPath = mapProps.getUpFilePath();
    	
    	String separator =System.getProperty("file.separator");
    	String fileDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    	
    	String fileDirPath =rootPath+separator+fileDir;
    	
    	File currFile=new File(fileDirPath);
    	
    	try {
    		if(!currFile.exists()){
    			FileUtils.forceMkdir(currFile);
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null; 
        
        List<Map<String, Object>> fileList=new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> ossMapList= new ArrayList<Map<String, Object>>();
        
        if(!StringUtils.isEmpty(ossKey)){
	        Map<String,Object> paramMap =new HashMap<String, Object>();
	        paramMap.put("ossKey", ossKey);
	        String paramString= jsonService.toJson(paramMap);
	        Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("E06012", paramString);
	        if(dataMap.containsKey("select")){
	        	ossMapList = (List<Map<String, Object>>)dataMap.get("select");	        	
			}    
	        System.out.println(JSONObject.toJSON(ossMapList));
        }
        FileOutputStream out=null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String imgFilePath ="";
                    int file_typenum=1;
                	String fileName =file.getOriginalFilename().replaceAll("[\\s'\"]+", "_");
                	long  fileSize = file.getSize()/1024;
                	String targetFilePath = fileDirPath +separator +fileNamePrefix+"_"+fileName;
                	File targetFile =new File(targetFilePath);
                    InputStream in = file.getInputStream(); 
	                out = new FileOutputStream(targetFile);	               
	                byte buffer[] = new byte[1024]; 
	                MessageDigest md = MessageDigest.getInstance("MD5");
	                int len = 0; 
	                while((len=in.read(buffer))>0){ 
	                    out.write(buffer, 0, len);
	                    md.update(buffer, 0, len);
	                } 
	                byte[] b = md.digest();
	                BigInteger bi = new BigInteger(1, b);
	                String md5=bi.toString(16);
	                System.out.println(md5);
	                System.out.println("接受完成");
	                in.close(); 
	                out.close(); 
	               /* if(!StringUtils.isEmpty(selbusinessCode)) {//根据md5来存储
	                	  JSONObject json=new JSONObject();
	                	  json.put("md5", md5);
	                	  Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(selbusinessCode, json.toJSONString());
	                	  if(dataMap!=null&&dataMap.containsKey("dataList")) {
	                		  if(dataMap.get("dataList")!=null) {
	                			  List<Map<String, Object>>listmap=(List<Map<String, Object>>) dataMap.get("dataList");
	                			  if(!listmap.isEmpty()) {
	                				  Map<String, Object> fileMap=new HashMap<String, Object>();
		                	        	fileMap.put("fileList", listmap);
			                		  return  APIResult.createSuccess( jsonService.toJson(fileMap)); 
	                			  }
	                		  }
	                	  }
	                }*/
	               
					String newFilePath = fileDir+"/"+fileNamePrefix+"_"+fileName;
                    Map<String, Object> fileMap=new HashMap<String, Object>();
                    String fileExt = newFilePath.substring(newFilePath.lastIndexOf(".")+1, newFilePath.length());
                    String fileNameWithoutExt = fileName.substring(0,fileName.lastIndexOf("."));
                
                    String storageFilePath = newFilePath;
                    if(!StringUtils.isEmpty(ossKey)){
                    	if("no".equals(substep)) {
                    		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
                        			transferFile(ossMapList,targetFilePath, newFilePath);
                    	}else {
                    		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
                        			transferFileJust(ossMapList,targetFilePath, newFilePath);
                    	}
                    }else {
                    	storageFilePath=targetFilePath;
                    }
                    imgFilePath=storageFilePath;
                   long audio_time=0;
                    if(video2img.isAudioFile(fileName)) {//音乐
                    	if("no".equals(substep)) {
                    	   audio_time=video2img.fetchLength(targetFilePath);
                    	}
                    	file_typenum=2;
                    	imgFilePath="../img/video/yinpin.png";
                    }else if(video2img.isVideoFile(fileName)){//视频
                    	//String localFilePath = video2img.fetchFrame(targetFilePath);
                    	//audio_time=video2img.fetchLength(targetFilePath);
                    	file_typenum=3;
                    	if("no".equals(substep)) {
                    		Map<String,Object> map=video2img.fetchAll(targetFilePath);
                        	String localFilePath =(String)map.get(CommonConstants.VOIDE_IMG);
                        	audio_time=(long)map.get(CommonConstants.VOIDE_LONG);
    						 if(!StringUtils.isEmpty(ossKey)){
    							 imgFilePath=ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
    									 transferFile(ossMapList,localFilePath, newFilePath.substring(0,newFilePath.lastIndexOf("."))+".jpg");
    					       new File(localFilePath).delete();
    						 }
                    	}else {
                    		imgFilePath=video2img.fetchFrameJust(storageFilePath);
                        	String imgrelativePath=video2img.fetchFrameJust(newFilePath);//图片相对路径
                        	 if(!StringUtils.isEmpty(ossKey)){
                        		 ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
                        		 .copyFile(ossMapList,"video.png",imgrelativePath); 
                        	 }
                    	}
                    }
                    
                    	
                    if(!StringUtils.isEmpty(ossKey)&&file_typenum==1) {
                    	ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
						.transferFile(ossMapList, targetFilePath, newFilePath);
                    	targetFile.delete(); 
                    }
                    
                    if(!StringUtils.isEmpty(ossKey)&&"no".equals(substep)){
                    	targetFile.delete();    
                    }
                    System.out.println("");
                    fileMap.put(CommonConstants.FILE_UP_ID, UUID.randomUUID().toString().replace("-", ""));
                    fileMap.put("fid", fId);
                    fileMap.put(CommonConstants.FILE_UP_AUDIO_TIME, audio_time);//音频时长
                    fileMap.put("file_type", fileExt);//文件后缀
                    fileMap.put(CommonConstants.FILE_UP_FILE_TYPENUM, file_typenum);//类型
                    fileMap.put("file_name", fileNameWithoutExt);//文件名称
                    fileMap.put("file_path", storageFilePath);//文件访问路径
                    fileMap.put("relative_path", newFilePath);//文件保存相对路径
                    fileMap.put(CommonConstants.FILE_UP_SAVE_PATH, targetFilePath);//文件保存liunx相对路径
                    fileMap.put("file_size", fileSize);//文件大小
                    fileMap.put("img_path", imgFilePath);//如果是视频，就会有视频路径
                    fileMap.put("md5", md5);//Md4
                    fileMap.put("upType", upType);//Md4
                    fileMap.put("tableName", tableName);
                    if(item!=null) {
                    	fileMap.put("item", item);
                    }
                    System.out.println("开始处理视频API");
                    if(!"no".equals(substep)) {
                    	 fileUploadHandle.fileUp(fileMap,ossMapList,upbusinessCode);
                    }
                    System.out.println("开始处理视频API2");
                    fileList.add(fileMap);
                    
					
                } catch (Exception e) {
                	out = null;
                    return APIResult.createInstance("上传失败:"+e.getMessage()) ;
                }
            } else {
                return APIResult.createInstance("上传失败-文件为空") ;
            }
        }
        
        String fileJsonString="{}";
    
        try{
        	Map<String, Object> fileMap=new HashMap<String, Object>();
        	fileMap.put("fileList", fileList);
        	if(item!=null) {
        		fileMap.put("fileList", fileList);	
        	}
        	fileJsonString= jsonService.toJson(fileMap);
        	
	        if(fileList.size()>0&& (!StringUtils.isEmpty(businessCode))){	        
	        	Map<String, Object> retMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode, fileJsonString);
	        	if(retMap==null || retMap.containsKey("hasErr")){
	        		return APIResult.createInstance("上传失败:后台"+retMap.get("hasErr"));
		        }
	        }
        }
        catch(Exception e){
        	e.printStackTrace();
        	return APIResult.createInstance("上传失败:"+e.getMessage());        	
        }
         
       
       return APIResult.createSuccess(fileJsonString);
    }
    //综合下载
    @RequestMapping("/download")
    public Object zipDownloadFile( HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value = "businessCode", required = true) String businessCode,
			//@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "downloadType", required = true) String downloadType,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
    		//Map<String, Object> paramsMap= jsonService.parseMap(params);
		String validateResult = validate(businessCode, params);
		
		System.err.println(downloadType+"--down-----"+params+"==param====="+businessCode);
		
		if (!StringUtils.isEmpty(validateResult)) {
			return APIResult.createInstance(validateResult);
		}
		params = RequestParamsUtils.rebuildParam(request, params);
		Map<String, Object> datas = getByDataBusinessCode(businessCode, params);
		if (datas != null && datas.containsKey(ErrorCodeConstants.HASH_ERR)) {
			return APIResult.createInstance(datas.get(ErrorCodeConstants.HASH_ERR).toString());
		}
		if (datas != null && datas.containsKey("dataList")) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) datas.get("dataList");
			if (mapList.size() >= 1) {
				byte[] buffer = new byte[10240];
			  
				OutputStream os = null;
				try {
					os = response.getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (downloadType.equals("zip")) {
					ZipOutputStream zos = new ZipOutputStream(os);
					byte[] buf = new byte[8192];
			        int len;
			        response.setContentType("application/force-download");
			        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
			        String zipFileName = formatter.format(new Date())+".zip";
			        try {
                        zipFileName=new String(zipFileName.getBytes("utf-8"), "ISO8859-1");
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
			        response.addHeader("Content-Disposition","attachment;fileName="+zipFileName);
			        response.setCharacterEncoding("UTF-8");
					for (int i = 0; i < mapList.size(); i++) {
						Map<String, Object> rowMap = mapList.get(i);
						FileInputStream fis = null;
						try {
						    File file = new File( (String) rowMap.get("save_path") );
				            if ( !file.isFile() ) continue;
				            ZipEntry ze = new ZipEntry( file.getName() );
				            zos.putNextEntry( ze );
				            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
				            while ( ( len = bis.read( buf ) ) > 0 ) {
				                zos.write( buf, 0, len );
				            }
				            bis.close();
				            zos.closeEntry();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}catch (IOException e) {
                            e.printStackTrace();
                        }
					}
					try {
						zos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					FileInputStream fis = null;
					BufferedInputStream bis = null;
				
					try {
					    File file = new File( (String)  mapList.get(0).get("save_path") );
                       // if ( !file.isFile() ) continue;
					    response.setContentType("application/force-download");
					    response.addHeader("Content-Disposition","attachment;fileName=" +  new String( file.getName().getBytes("utf-8"), "ISO8859-1"));
						fis = new FileInputStream(file);
						bis = new BufferedInputStream(fis);
						int i = bis.read(buffer);
						while (i != -1) {
							os.write(buffer, 0, i);
							i = bis.read(buffer);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (bis != null) {
							try {
								bis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (fis != null) {
							try {
								fis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		}
		return null;
    }
    public static void main(String[] args) {
        List<String> list =new ArrayList();
        String[] strs = new String[5];
        StringBuffer sb=new StringBuffer();
        try{
            sb.append("D:/log/networm-2018-12-12.log,");
            sb.append("D:/log/networm-2018-12-13.log");
            writeZip(sb,"newZipFile");
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
    private static void writeZip(StringBuffer sb,String zipname) throws IOException {
        String[] files = sb.toString().split(",");
        System.out.println(files);
        OutputStream os = new BufferedOutputStream( new FileOutputStream( zipname+".zip" ) );
        ZipOutputStream zos = new ZipOutputStream( os );
        byte[] buf = new byte[8192];
        int len;
        for (int i=0;i<files.length;i++) {  
            File file = new File( files[i] );
            if ( !file.isFile() ) continue;
            ZipEntry ze = new ZipEntry( file.getName() );
            zos.putNextEntry( ze );
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
            while ( ( len = bis.read( buf ) ) > 0 ) {
                zos.write( buf, 0, len );
            }
            zos.closeEntry();
        }
//        zos.setEncoding("GBK");
        zos.closeEntry();
        zos.close();
        
        /*for(int i=0;i<files.length;i++){
         System.out.println("------------"+files );
         File file= new File(files[i] );
         file.delete();
        }*/
    }
    
    
  //阿里云上传
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/aliyun/upload", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public Object aliyunFileUpload(HttpServletRequest request) {
//    	String storeId = request.getParameter("storeId");
    	String upType = request.getParameter("upType");
    	String fId = request.getParameter("fId");
    	String businessCode = request.getParameter("businessCode");
//      	String selbusinessCode = request.getParameter("selBusinessCode");
    	String upbusinessCode = request.getParameter("upBusinessCode");
    	String ossKey = request.getParameter("ossKey");
    	String substep = request.getParameter("substep");
    	String item = request.getParameter("item");
    	String tableName = request.getParameter("tableName");
    	String rootPath = mapProps.getUpFilePath();
    	
    	String separator =System.getProperty("file.separator");
    	String fileDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    	
    	String fileDirPath =rootPath+separator+fileDir;
    	
    	File currFile=new File(fileDirPath);
    	
    	try {
    		if(!currFile.exists()){
    			FileUtils.forceMkdir(currFile);
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null; 
        
        List<Map<String, Object>> fileList=new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> ossMapList= new ArrayList<Map<String, Object>>();
        
        if(!StringUtils.isEmpty(ossKey)){
	        Map<String,Object> paramMap =new HashMap<String, Object>();
	        paramMap.put("ossKey", ossKey);
	        String paramString= jsonService.toJson(paramMap);
	        Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("E06012", paramString);
	        if(dataMap.containsKey("select")){
	        	ossMapList = (List<Map<String, Object>>)dataMap.get("select");	        	
			}    
	        System.out.println(JSONObject.toJSON(ossMapList));
        }
        FileOutputStream out=null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    String imgFilePath ="";
                    int file_typenum=1;
                	String fileName =file.getOriginalFilename().replaceAll("[\\s'\"]+", "_");
                	long  fileSize = file.getSize()/1024;
                	String targetFilePath = fileDirPath +separator +fileNamePrefix+"_"+fileName;
                	File targetFile =new File(targetFilePath);
                    InputStream in = file.getInputStream(); 
	                out = new FileOutputStream(targetFile);	               
	                byte buffer[] = new byte[1024]; 
	                MessageDigest md = MessageDigest.getInstance("MD5");
	                int len = 0; 
	                while((len=in.read(buffer))>0){ 
	                    out.write(buffer, 0, len);
	                    md.update(buffer, 0, len);
	                } 
	                byte[] b = md.digest();
	                BigInteger bi = new BigInteger(1, b);
	                String md5=bi.toString(16);
	                System.out.println(md5);
	                System.out.println("接受完成");
	                in.close(); 
	                out.close(); 
	              
	               
					String newFilePath = fileDir+"/"+fileNamePrefix+"_"+fileName;
                    Map<String, Object> fileMap=new HashMap<String, Object>();
                    String fileExt = newFilePath.substring(newFilePath.lastIndexOf(".")+1, newFilePath.length());
                    String fileNameWithoutExt = fileName.substring(0,fileName.lastIndexOf("."));
                
                    String storageFilePath = newFilePath;
                    if(!StringUtils.isEmpty(ossKey)){
                    	if("no".equals(substep)) {
                    		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
                        			transferFile(ossMapList,targetFilePath, newFilePath);
                    	}else {
                    		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
                        			transferFileJust(ossMapList,targetFilePath, newFilePath);
                    	}
                    }else {
                    	storageFilePath=targetFilePath;
                    }
                    imgFilePath=storageFilePath;
                   long audio_time=0;
                    if(video2img.isAudioFile(fileName)) {//音乐
                    	if("no".equals(substep)) {
                    	   audio_time=video2img.fetchLength(targetFilePath);
                    	}
                    	file_typenum=2;
                    	imgFilePath="/img/video/yinpin.png";
                    }else if(video2img.isVideoFile(fileName)){//视频
                    	//String localFilePath = video2img.fetchFrame(targetFilePath);
                    	//audio_time=video2img.fetchLength(targetFilePath);
                    	file_typenum=3;
                    	if("no".equals(substep)) {
                    		Map<String,Object> map=video2img.fetchAll(targetFilePath);
                        	String localFilePath =(String)map.get(CommonConstants.VOIDE_IMG);
                        	audio_time=(long)map.get(CommonConstants.VOIDE_LONG);
    						 if(!StringUtils.isEmpty(ossKey)){
    							 imgFilePath=ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
    									 transferFile(ossMapList,localFilePath, newFilePath.substring(0,newFilePath.lastIndexOf("."))+".jpg");
    					       new File(localFilePath).delete();
    						 }
                    	}else {
                    		imgFilePath=video2img.fetchFrameJust(storageFilePath);
                        	String imgrelativePath=video2img.fetchFrameJust(newFilePath);//图片相对路径
                        	 if(!StringUtils.isEmpty(ossKey)){
                        		 ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
                        		 .copyFile(ossMapList,"video.png",imgrelativePath); 
                        	 }
                    	}
                    }
                    
                    	
                    if(!StringUtils.isEmpty(ossKey)&&file_typenum==1) {
                    	ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
						.transferFile(ossMapList, targetFilePath, newFilePath);
                    	targetFile.delete(); 
                    }
                    
                    if(!StringUtils.isEmpty(ossKey)&&"no".equals(substep)){
                    	targetFile.delete();    
                    }
                    System.out.println("");
                    fileMap.put(CommonConstants.FILE_UP_ID, UUID.randomUUID().toString().replace("-", ""));
                    fileMap.put("fid", fId);
                    fileMap.put(CommonConstants.FILE_UP_AUDIO_TIME, audio_time);//音频时长
                    fileMap.put("file_type", fileExt);//文件后缀
                    fileMap.put(CommonConstants.FILE_UP_FILE_TYPENUM, file_typenum);//类型
                    fileMap.put("file_name", fileNameWithoutExt);//文件名称
                    fileMap.put("file_path", storageFilePath);//文件访问路径
                    fileMap.put("relative_path", newFilePath);//文件保存相对路径
                    fileMap.put(CommonConstants.FILE_UP_SAVE_PATH, targetFilePath);//文件保存liunx相对路径
                    fileMap.put("file_size", fileSize);//文件大小
                    fileMap.put("img_path", imgFilePath);//如果是视频，就会有视频路径
                    fileMap.put("md5", md5);//Md4
                    fileMap.put("upType", upType);//Md4
                    fileMap.put("tableName", tableName);
                    if(item!=null) {
                    	fileMap.put("item", item);
                    }
                    System.out.println("开始处理视频API");
                    if(!"no".equals(substep)) {
                    	 fileUploadHandle.fileUp(fileMap,ossMapList,upbusinessCode);
                    }
                    System.out.println("开始处理视频API2");
                    fileList.add(fileMap);
                    
					
                } catch (Exception e) {
                	out = null;
                    return APIResult.createInstance("上传失败:"+e.getMessage()) ;
                }
            } else {
                return APIResult.createInstance("上传失败-文件为空") ;
            }
        }
        
        String fileJsonString="{}";
    
        try{
        	Map<String, Object> fileMap=new HashMap<String, Object>();
        	fileMap.put("fileList", fileList);
        	if(item!=null) {
        		fileMap.put("fileList", fileList);	
        	}
        	fileJsonString= jsonService.toJson(fileMap);
        	
	        if(fileList.size()>0&& (!StringUtils.isEmpty(businessCode))){	        
	        	Map<String, Object> retMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode, fileJsonString);
	        	if(retMap==null || retMap.containsKey("hasErr")){
	        		return APIResult.createInstance("上传失败:后台"+retMap.get("hasErr"));
		        }
	        }
        }
        catch(Exception e){
        	e.printStackTrace();
        	return APIResult.createInstance("上传失败:"+e.getMessage());        	
        }      
       return APIResult.createSuccess(fileJsonString);
    }
}
