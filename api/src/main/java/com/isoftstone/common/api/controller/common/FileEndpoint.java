package com.isoftstone.common.api.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.isoftstone.common.api.service.common.FileUploadHandle;
import com.isoftstone.common.api.service.oss.OssStorageFactory;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.api.util.ExcelUtils;
import com.isoftstone.common.api.util.FileUtil;
import com.isoftstone.common.api.util.Video2ImgUtils;
import com.isoftstone.common.api.util.WordUtils;
import com.isoftstone.common.api.util.bean.FileSaveInfo;
import com.isoftstone.common.plugins.visua.VisuaSqlExample;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import com.isoftstone.common.util.MyProps;
import org.apache.commons.io.FileUtils;
import org.common.constant.ApiMapperUrlConstants;
import org.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping(ApiMapperUrlConstants.FILE)
public class FileEndpoint {
	@Autowired
    MyProps mapProps;
	@Autowired
	ExcelUtils excelUtils;
	@Autowired
	JsonService jsonService;
	@Value("${excelfile.import-rows:50}")
	int import_rows;
	@Autowired
	Video2ImgUtils video2img;
	@Autowired
	FileUploadHandle fileUploadHandle;
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;
	@Autowired
	OssStorageFactory ossStorageFactory;
	
	//文件上传相关代码
    //@RequestMapping(value = "upload")
    public Object upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return APIResult.createInstance("文件不能为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件上传后的路径
        String filePath =mapProps.getUpFilePath();
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
//            return  APIResult.createSuccess("上传成功");
//            返回文件存储路径
            return APIResult.createSuccess(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return APIResult.createInstance("上传失败");
    }
    //文件下载相关代码
    @RequestMapping("/download")
    public String downloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request
    		, HttpServletResponse response,String fileName){
        if (fileName != null) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
            String realPath = mapProps.getDownFilePath();
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" +  fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
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
        return null;
    }
   
    
    @PostMapping("/upload")
    public Object handleFileU(HttpServletRequest request,
    		String guid,
    		String md5value,
    		@RequestParam(value = "chunks",required = false)String chunks,
    		@RequestParam(value = "chunk",required = false)String chunk,
            @RequestParam(value = "name",required = false)String name,
            @RequestParam(value = "substep",required = false)String substep,//是否异步上传到云端
            @RequestParam(value = "storeId",required = false)String storeId,//门店ID
            @RequestParam(value = "ossKey",required = false)String ossKey,//云端上传key
            @RequestParam(value = "businessCode",required = false)String businessCode,//云端上传key
            @RequestParam(value = "selBusinessCode",required = false)String selBusinessCode,//云端上传key
            @RequestParam(value = "upBusinessCode",required = false)String upBusinessCode,//更新的
    		@RequestParam(value = "file",required = false) MultipartFile file) throws Exception {
    	System.out.println("上传");
    	System.out.println(file.getName());
    	if(!StringUtils.isEmpty(md5value)&&!StringUtils.isEmpty(selBusinessCode)) {//先查询md5看有没有值
    		JSONObject json=new JSONObject();
	      	  json.put("md5", md5value);
	      	 json.put("store_id", storeId);
	      	  Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(selBusinessCode, json.toJSONString());
	      	  if(dataMap!=null&&dataMap.containsKey("dataList")) {
	      		  if(dataMap.get("dataList")!=null) {
	      			  List<Map<String, Object>>listmap=(List<Map<String, Object>>) dataMap.get("dataList");
	      			  if(!listmap.isEmpty()) {
	      				  Map<String, Object> fileMap=new HashMap<String, Object>();
	          	        	fileMap.put("fileList", listmap);
	              		     return  APIResult.createSuccess(jsonService.toJson(fileMap)); 
	      			  }
	      		  }
	      	  }
    	  }
    	    String fileDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
    		String uploadFolderPath = mapProps.getUpFilePath()+"/"+fileDir;//上传目录
    	     String ext = "";
    		 String fileName;
    	     int temp = guid.lastIndexOf(".");
    	     if (temp != -1) {
    	          guid = guid.substring(0, temp);
    	     }
    	     String mergePath =uploadFolderPath  + "/" + guid + "/";
    	     
    	     if(name.lastIndexOf(".") != -1) {
    	           ext = name.substring(name.lastIndexOf("."));
    	      }
    		int index;
    	   Map<String, Object> fileMap=new HashMap<String, Object>();
    		if (chunks != null && chunk != null && !chunks.equals("1")) {
    	          index = Integer.parseInt(chunk);
    	          fileName = String.valueOf(index) + ext;
    	          // 将文件分块保存到临时文件夹里，便于之后的合并文件
    	          FileUtil.saveFile(mergePath, fileName, file);
    	          // 验证所有分块是否上传成功，成功的话进行合并
    	          FileSaveInfo saveInfo = FileUtil.uploaded(md5value,guid, chunk, chunks, uploadFolderPath, fileName, ext,fileDir,name);
    	        //合并成功的话，将文件上传的云服务器
    	          if(saveInfo!=null) {
    	        	  fileSave(fileMap,saveInfo,storeId,md5value,substep.equals("no")?true:false,ossKey,upBusinessCode,businessCode);
    	        	  return  APIResult.createSuccess(jsonService.toJson(fileMap));
    	          }
    	      } else {
    	    	  String savePath = uploadFolderPath + File.separator;
    	    	  fileName = guid + ext;
    	    	  FileUtil.saveFile(savePath, fileName, file);
    	    	  FileSaveInfo saveInfo = new FileSaveInfo();
       	          saveInfo.setName(name);
       	          saveInfo.setFix(ext);
       	          saveInfo.setSize(file.getSize());
       	          saveInfo.setContentType(file.getContentType());
       	          saveInfo.setSaveName(fileName);
       	          saveInfo.setPath(savePath+fileName);
       	          saveInfo.setRelativePath(fileDir+"/" +  guid + ext);
    	    	  fileSave(fileMap,saveInfo,storeId,md5value,substep.equals("no")?true:false,ossKey,upBusinessCode,businessCode);
	        	  return  APIResult.createSuccess(jsonService.toJson(fileMap));
    	      }
    	return null;
    }
    /**
     * 
     * @param fileMap
     * @param saveInfo 合并后文件的描述类
     * @param storeId  
     * @param md5
     * @param substep 是否异步上传
     * @param ossKey
     */
    private void fileSave(Map<String, Object> fileMap, FileSaveInfo saveInfo,
    		String storeId,String md5,boolean substep,String ossKey,String upBusinessCode,String businessCode) {
    	 String imgFilePath ="";
         int file_typenum=1;
//         String targetFile=saveInfo.getPath();
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
    	String newFilePath = saveInfo.getRelativePath();//本地相对路径
    	String storageFilePath = newFilePath;
    	String targetFilePath = saveInfo.getPath();//本地绝对路径
    	File targetFile =new File(targetFilePath);
    	
    	if(saveInfo.getPath()!=null&&!saveInfo.getPath().isEmpty()) {
    		if(!StringUtils.isEmpty(ossKey)){
            	if(substep) {
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
            if(video2img.isAudioFile(saveInfo.getFix())) {//音乐
            	if(substep) {
            	   try {
					audio_time=video2img.fetchLength(targetFilePath);
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	}
            	file_typenum=2;
            	imgFilePath="../img/video/yinpin.png";
            }else if(video2img.isVideoFile(saveInfo.getFix())){//视频
            	//String localFilePath = video2img.fetchFrame(targetFilePath);
            	//audio_time=video2img.fetchLength(targetFilePath);
            	file_typenum=3;
            	if(substep) {
            		Map<String, Object> map;
						try {
							map = video2img.fetchAll(targetFilePath);
                	String localFilePath =(String)map.get(CommonConstants.VOIDE_IMG);
                	audio_time=(long)map.get(CommonConstants.VOIDE_LONG);
                	
					 if(!StringUtils.isEmpty(ossKey)){
						 imgFilePath=ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
								 transferFile(ossMapList,localFilePath, 
										 newFilePath.substring(0,newFilePath.lastIndexOf("."))+".jpg");
				            new File(localFilePath).delete();
					  }
						} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            	}else {
            		try {
						imgFilePath=video2img.fetchFrameJust(storageFilePath);//网络图片路径
                	String imgrelativePath=video2img.fetchFrameJust(newFilePath);//图片相对路径
                	 if(!StringUtils.isEmpty(ossKey)){
                		 ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
                		 .copyFile(ossMapList,"video.png",imgrelativePath); 
                	 }
            		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }else if(video2img.isPicFile(saveInfo.getFix())) {//图片
            	file_typenum=1;
//            	if(substep) {
//              		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
//                  			transferFile(ossMapList,storageFilePath, newFilePath);
//              	}else {
//              		storageFilePath= ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
//                  			transferFileJust(ossMapList,saveInfo.getPath(), newFilePath);
//              	}
            }
            
            	
            if(!StringUtils.isEmpty(ossKey)&&file_typenum==1) {
            	ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
				.transferFile(ossMapList, targetFilePath, newFilePath);
            	targetFile.delete();
            }
            
            if(!StringUtils.isEmpty(ossKey)&&substep){
            	targetFile.delete();
            }
            Map<String, Object> map=new HashMap<String, Object>();
            map.put(CommonConstants.FILE_UP_ID, UUID.randomUUID().toString().replace("-", ""));
            /*fileMap.put("fid", fId);*/
            map.put(CommonConstants.FILE_UP_AUDIO_TIME, audio_time);//音频时长
            map.put("file_type", saveInfo.getFix().substring(1));//文件后缀
            map.put(CommonConstants.FILE_UP_FILE_TYPENUM,file_typenum);//类型
            map.put("file_name", saveInfo.getName().substring(0, saveInfo.getName().indexOf(".")));//文件名称
            map.put("file_path", storageFilePath);//文件访问路径
            map.put("relative_path", newFilePath);//文件保存相对路径
            map.put(CommonConstants.FILE_UP_SAVE_PATH, targetFilePath);//文件保存liunx相对路径
            map.put("file_size", saveInfo.getSize()/1024);//文件大小
            map.put("img_path", imgFilePath);//如果是视频，就会有视频路径
            map.put("md5", md5);//Md4
            map.put("store_id", storeId);
            if(!substep) {
            	 fileUploadHandle.fileUp(map,ossMapList,upBusinessCode);
            }
            System.out.println("开始处理视频API2");
            fileList.add(map);
    	}else {
            System.out.println("上传失败-文件为空");
        }
    	
    	String fileJsonString="{}";
        
        try{
        	fileMap.put("fileList", fileList);
        	fileJsonString= jsonService.toJson(fileMap);
        	
	        if(fileList.size()>0&& (!StringUtils.isEmpty(businessCode))){	        
	        	Map<String, Object> retMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode, fileJsonString);
	        	if(retMap==null || retMap.containsKey("hasErr")){
	        		System.out.println("上传失败:后台"+retMap.get("hasErr"));
//	        		return APIResult.createInstance("上传失败:后台"+retMap.get("hasErr"));
		        }
	        }
        }
        catch(Exception e){
        	e.printStackTrace();
//        	return APIResult.createInstance("上传失败:"+e.getMessage());        	
        }    	
    }
	//多文件上传
    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
    public Object handleFileUpload(HttpServletRequest request) {
    	String storeId = request.getParameter("storeId");
    	String upType = request.getParameter("upType");
    	String fId = request.getParameter("fId");
    	String businessCode = request.getParameter("businessCode");
      	String selbusinessCode = request.getParameter("selBusinessCode");
    	String upbusinessCode = request.getParameter("upBusinessCode");
    	String ossKey = request.getParameter("ossKey");
    	String substep = request.getParameter("substep");
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
	                if(!StringUtils.isEmpty(selbusinessCode)) {//根据md5来存储
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
	                }
	               
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
                    fileMap.put("store_id", storeId);
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
    //云端资源删除
    @RequestMapping(value = "/batch/delete", method = RequestMethod.POST)
    public Object handleFileDelete(HttpServletRequest request,
    		@RequestParam(value = "businessCode", required = true) String businesscode,
			@RequestParam(value = "params", required = false, defaultValue = "{}") String params) {
    	Map<String, Object> datas = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businesscode, params);//接口为 D02040，参数 id_list，store_id
    	
       	String ossKey = request.getParameter("ossKey");
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
    	String rootPath = mapProps.getUpFilePath();
    	String separator =System.getProperty("file.separator");
    	String fileDirPath =rootPath+separator;
        if (datas != null && datas.containsKey("dataListYunduan")) {
    		List<Map<String, Object>> list = (List<Map<String, Object>>) datas.get("dataListYunduan");
    		String key_name = null;
    		for (Map<String, Object> map2 : list) {
    			key_name = (String) map2.get("relative_path");
    			String fileDirPath1=fileDirPath+key_name;
    			   if(!StringUtils.isEmpty(ossKey)){
		    			//删除云端资源
    				   fileDirPath1=ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString()).
    						   deleteFile(ossMapList,key_name);
    			   }else {
    				   new File(fileDirPath1).delete();
    			   }
    		}
    	}
         return APIResult.createSuccess();
    }
  //文件上传相关代码
    @RequestMapping(value = "uploadExcel")
    public Object uploadExcel(@RequestParam("file") MultipartFile file,String businessCode,String businessCode2,String ossType,
    		@RequestParam(value = "store_id", required = false) String store_id,
    		@RequestParam(value = "id", required = false) String id
    		) {
    	System.out.println(id);
        if (file.isEmpty()) {
            return APIResult.createInstance("文件不能为空");
        }
       /* String contentType = "application/vnd.ms-excel";
        if (!contentType.equals(file.getContentType())) {
        	 return APIResult.createInstance("文件格式错误");
        }*/
      
        try {   
        	   List<Map<String, Object>> ossMapList= new ArrayList<Map<String, Object>>();
               
               if(!StringUtils.isEmpty(ossType)){
       	        Map<String,Object> paramMap =new HashMap<String, Object>();
       	        paramMap.put("ossType", ossType);
       	        String paramString= jsonService.toJson(paramMap);
       	        Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode("E06006", paramString);
       	        if(dataMap.containsKey("select")){
       	        	ossMapList = (List<Map<String, Object>>)dataMap.get("select");	        	
       			}
               }
        	System.out.println(businessCode);
            VisuaSqlExample visuaSqlExample = hystrixVisuaSqlExampleClient.getByBusinessCode(businessCode);
            String inputData = visuaSqlExample.getInputData();
            Map<String, Object> map = excelUtils.getDataFromExcel(ossType,ossMapList,file, inputData);
            if(map.containsKey("hasErr")) {
            	String string = map.get("hasErr").toString().replace("=", " ");
            	return APIResult.createInstance(string.substring(1,string.length()-2));
            }
            //判断map里的值 不重复
            List<Map<String, Object>> object = (List<Map<String, Object>>) map.get("dataList");
            Map<String,Integer> objectmap =new HashMap<String, Integer>();
            StringBuffer sb=new StringBuffer();
            for (Map<String, Object> map2 : object) {
            	if(map2.containsKey("hasErr")) {
            		return APIResult.createInstance(map2.get("hasErr").toString());
            	}
            	String useracct=(String) map2.get("useracct");
            	map2.put("store_id", store_id);
            	map2.put("temp_id", id);
            	if(objectmap.containsKey(useracct)) {
            		int count=objectmap.get(useracct);
            		if(count==1) {sb.append(useracct).append(",");}
            		objectmap.put(useracct,count +1);
            	}else {
            		objectmap.put(useracct, 1);
            	}
			}
            if(objectmap.size()!=object.size()) {
            	CharSequence subSequence = sb.subSequence(0, 0);
            	 return APIResult.createInstance(sb.substring(0,sb.length() - 1)+"员工编号重复");
            }
            
            Map<String, Object> checkData = checkData(map, businessCode2);
            if(checkData.containsKey("hasErr")) {
            	return APIResult.createInstance(checkData.get("hasErr").toString());
            }
            System.out.println(map.get("dataList"));
            map.get("dataList");
            Map<String, Object> resultMap= saveDataInfoList(map, businessCode);
            
            return APIResult.createSuccess(resultMap);
        } catch (Exception e) {
          return  APIResult.createInstance("导入失败"+e.getMessage());
        }
    }
   
   /*@RequestMapping(value = "uploadExcel1")
    public void uploadExcel(String businessCode) {    	
    	   long startTime = System.currentTimeMillis();
    	   VisuaSqlExample visuaSqlExample = hystrixVisuaSqlExampleClient.getByBusinessCode(businessCode);
           String inputData = visuaSqlExample.getInputData();
           Map<String, Object> map = excelUtils.getDataFromExcel("E:\\告警1.xlsx", inputData);
          
           int successCount = saveDataInfoList(map, businessCode);                                 
           
           long endTime = System.currentTimeMillis();
           
           long diffTime = endTime-startTime;
           
           System.out.println("导入次数："+successCount+" 导入用时 :"+diffTime);
           
    }*/
    
    public class ExcelImportThread implements Callable<Integer>{  
    	String key=null;
    	private List<Map<String, Object>> mapList=null;
    	private String businessCode;
    	CountDownLatch end;
    	public ExcelImportThread( CountDownLatch end ,String key,List<Map<String, Object>> list,String businessCode) {
    		this.key=key;
    		this.end=end;
    		mapList=list;
    		this.businessCode=businessCode;
		}
    	 
		@Override
		public Integer call() throws Exception {
			int retValue=1;
			if(mapList.size()>0){
			   Map<String,Object>map=new HashMap<String, Object>();
	           map.put(key, mapList);
	           String paramsJson= jsonService.toJson(map);
	           Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode, paramsJson);
	           if(dataMap==null || dataMap.containsKey("hasErr")){
	        	   retValue=0;
	           }
			}
	        end.countDown();
			return retValue; 
		} 
    }
  
    public Map<String, Object> saveDataInfoList(Map<String, Object> map,String businessCode) {
    	Map<String, Object> resultMap=new HashMap<String, Object>();
    	List<Map<String, Object>> dataList = new LinkedList<Map<String,Object>>();
    	int successCount=0;
    	int failureCount=0;
    	String errMsg="";
    	for(String key:map.keySet()){
    		List<Map<String,Object>> list = (List<Map<String,Object>>)map.get(key);    		 
	        Integer row = 0;	        
	        int count = import_rows;// 一个线程处理50条数据
	        int listSize = list.size();// 数据集合大小
	        int runThreadSize = (listSize / count) + 1; // 开启的线程数
	        List<Map<String, Object>> newlist = null;// 存放每个线程的执行数据
	        ExecutorService executor = Executors.newFixedThreadPool(2);// 创建一个线程池
	 
	        // 创建两个个计数器
	        CountDownLatch begin = new CountDownLatch(1);
	        CountDownLatch end = new CountDownLatch(runThreadSize);
	         
	        // 循环创建线程
	        for (int i = 0; i < runThreadSize; i++) {
	        	int startIndex = (i * count);
	        	int endIndex = (i + 1) * count;
	            if ((i + 1) == runThreadSize) {	
	                endIndex = list.size();
	            } 
	            newlist = list.subList(startIndex, endIndex);
	      
	            ExcelImportThread excelImportThread =new ExcelImportThread(end,key,newlist,businessCode);          
	            Future<Integer> submit = (Future<Integer>) executor.submit(excelImportThread);
	            try {
	                //提交成功的次数
	            	row = submit.get();
	            	if("dataList".equals(key)){
	            		successCount += row*(endIndex-startIndex);	 
	            		for(int key1 =startIndex;key1<endIndex;key1++) {
	            			Map<String, Object> map2 = list.get(key1);
	            			dataList.add(map2);
	            		}  
		            	if(row<1){
		            		failureCount +=(endIndex-startIndex);	//(endIndex-startIndex) 为实际导入的行数	            	
			            	errMsg += String.format("第%d行到%d行数据未导入进去。", startIndex+2,endIndex+1);
		            	}
	            	}
	            } catch (Exception e1) {
	            	if("dataList".equals(key)){
		            	failureCount +=(endIndex-startIndex);	            	
		            	errMsg += String.format("第%d行到%d行数据未导入进去。", startIndex+2,endIndex+1);
	            	}
	               e1.printStackTrace();
	            }
	        }
	        try{
	            begin.countDown();
	            end.await();
	            //执行完关闭线程池
	            executor.shutdown();
	        }catch (Exception e) {
	             e.printStackTrace();
	        }
	    }
    	
    	
    	
    	Map<String, Object> failMap=new HashMap<String, Object>();    	
    	failMap.put("failureCount", failureCount);
    	failMap.put("errMsg", errMsg);
    	
    	resultMap.put("successdata", dataList);
    	resultMap.put("failure", failMap);
        return resultMap;
    }
    /**
     * 判断员工编号是否重复            
     */
    public Map<String, Object> checkData(Map<String, Object> map,String businessCode) {
          String paramsJson= jsonService.toJson(map);
          Map<String, Object> dataMap = hystrixVisuaSqlExampleClient.getByDataBusinessCode(businessCode, paramsJson);
          
          
        return dataMap;
    }
    
    /**
     * 导出为word文件
     * @param request 请求
     * @param response 响应
     * @param projectName 项目名称
     * @param templateName 模板名称
     * @param title 导出文件名称
     */
    @RequestMapping("/exportWord") 
    public void exportWord(HttpServletRequest request, HttpServletResponse response,
    		String projectName,String templateName,String title)
    { 
    	 // TODO  利用Map来组织数据
    	 Map<String, Object> map =new HashMap<String, Object>();
		 map.put("name", "111111111");		  	 
	 
		 String ftlFile=templateName.trim()+".ftl";	
		 String templateRootPath = mapProps.getExportWordPath();		 
		 templateRootPath += projectName;
		 
		 try {
			WordUtils.exportMillCertificateWord( request, response, map, templateRootPath, title, ftlFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    //文件打包zip下载
    @SuppressWarnings("unchecked")
	@RequestMapping("/zipDownload")
    public String zipDownloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request
    		, HttpServletResponse response, String fileName, String downloadType, String bescc,String parms) throws IOException{
    	if (fileName != null) {
            String realPath = mapProps.getDownFilePath();
            File file = new File(realPath, fileName);
            if (file.exists()) {
            	response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);
            	byte[] buffer = new byte[1024];
            	if("self".equals(downloadType)) {
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    try {
                        fis = new FileInputStream(file);
                        bis = new BufferedInputStream(fis);
                        OutputStream os = response.getOutputStream();
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
            	}else if("zip".equals(downloadType)) {
            		Map<String, Object> dataMap = (Map<String, Object>) hystrixVisuaSqlExampleClient.getByBusinessCode("E06043");
                	if(dataMap != null && dataMap.containsKey("dataList")) {
                		List<Map<String, Object>> mapList = (List<Map<String, Object>>) dataMap.get("dataList");
                		System.out.println(mapList.size());
                        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        for (Map<String, Object> map1 : mapList) {
                        	Set<String> keySet = map1.keySet();
                        	Map<String, Object> map2 = new HashMap<String, Object>();
                        	for (String key : keySet) {
                        		if("file_path".equals(key)) {
                        			Object filePath = map1.get("file_path");
                        			map2.put("file_path", filePath);
                        		}
                        		if("file_name".equals(key)) {
                        			Object fileName1 = map1.get("file_name");
                        			map2.put("fileName", fileName1);
                        		}
                        	}
                        	list.add(map2);
            			}
                        for (int i = 0; i < list.size(); i++) {
                            FileInputStream fis = new FileInputStream((String) list.get(i).get("file_path"));
                            zos.putNextEntry(new ZipEntry((String) list.get(i).get("file_name")));
                            int len;
                            while ((len = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);
                            }
                            zos.closeEntry();
                            fis.close();
                        }
                        zos.close();
                	}
            	}
        	}
        }
        return null;
    }
}
