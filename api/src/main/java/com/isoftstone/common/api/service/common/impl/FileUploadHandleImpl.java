package com.isoftstone.common.api.service.common.impl;

import com.isoftstone.common.api.service.common.FileUploadHandle;
import com.isoftstone.common.api.service.oss.OssStorageFactory;
import com.isoftstone.common.api.util.Video2ImgUtils;
import com.isoftstone.common.common.FileAttrDto;
import com.isoftstone.common.plugins.visua.hystrix.HystrixVisuaSqlExampleClient;
import com.isoftstone.common.util.JsonService;
import org.common.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class FileUploadHandleImpl implements FileUploadHandle {
	@Autowired
	Video2ImgUtils video2img;
	@Autowired
	OssStorageFactory ossStorageFactory;
	@Autowired
	JsonService jsonService;
	@Autowired
	HystrixVisuaSqlExampleClient hystrixVisuaSqlExampleClient;

	@Override
	public Object file(FileAttrDto fileAttrDto, Map<String, Object> ossMapList, String type, String upBusinessCode) {
		System.out.println("start executeAsync");
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end executeAsync");
		return null;
	}

	@Override
	@Async("asyncServiceExecutor")
	public void fileUp(Map<String, Object> fileMap, List<Map<String, Object>> ossMapList, String upbusinessCode) {
		if (!ossMapList.isEmpty()) {
			  System.out.println("开始处理视频APItttt");
			int file_typenum = (int) fileMap.get(CommonConstants.FILE_UP_FILE_TYPENUM);// 文件类型
			long audio_time = 0;
			String save_path = (String) fileMap.get(CommonConstants.FILE_UP_SAVE_PATH);
			String relative_path = (String) fileMap.get(CommonConstants.FILE_UP_RELATIVE_PATH);

			/*if (file_typenum == 1) {// 图片，直接上传
				String path = ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
						.transferFile(ossMapList, save_path, relative_path);
				new File(save_path).delete();
			} else*/
				if (file_typenum == 2) {// 音频
				try {
					fileUpUp(fileMap, ossMapList, upbusinessCode);
					audio_time = video2img.fetchLength(save_path);
					new File(save_path).delete();
					if(!StringUtils.isEmpty(upbusinessCode)) {
						fileMap.put(CommonConstants.FILE_UP_AUDIO_TIME, audio_time);
						System.out.println( jsonService.toJson(fileMap));
						 hystrixVisuaSqlExampleClient.
								getByDataBusinessCode(upbusinessCode, jsonService.toJson(fileMap));
					}
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
					e.printStackTrace();
				}
			} else if (file_typenum == 3) {// 视频--先给视频的图片截取一下上传，然后再上传视频文件，最后上传视频
				Map<String, Object> map;
				try {
					fileUpUp(fileMap, ossMapList, upbusinessCode);
					map = video2img.fetchAll(save_path);
					String localFilePath = (String) map.get(CommonConstants.VOIDE_IMG);
					audio_time = (long) map.get(CommonConstants.VOIDE_LONG);
					String path = ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
							.transferFile(ossMapList, localFilePath,
									relative_path.substring(0, relative_path.lastIndexOf(".")) + ".jpg");
					new File(save_path).delete();
				
					if(!StringUtils.isEmpty(upbusinessCode)) {
						fileMap.put(CommonConstants.FILE_UP_AUDIO_TIME, audio_time);
						System.out.println( jsonService.toJson(fileMap));
						Map<String, Object> retMap = hystrixVisuaSqlExampleClient.
								getByDataBusinessCode(upbusinessCode, jsonService.toJson(fileMap));
						if(retMap==null || retMap.containsKey("hasErr")){
							System.out.println("上传失败:后台"+retMap.get("hasErr"));
				        }
					}
						
              	 
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
					e.printStackTrace();
				}

			}
				  System.out.println("结束处理视频APItttt");
		}
	}

	//@Async("asyncUpServiceExecutor")
	public void fileUpUp(Map<String, Object> fileMap, List<Map<String, Object>> ossMapList, String upbusinessCode) {
		if (!ossMapList.isEmpty()) {
			int file_typenum = (int) fileMap.get(CommonConstants.FILE_UP_FILE_TYPENUM);// 文件类型
			long audio_time = 0;
			String save_path = (String) fileMap.get(CommonConstants.FILE_UP_SAVE_PATH);
			String relative_path = (String) fileMap.get(CommonConstants.FILE_UP_RELATIVE_PATH);

			if (file_typenum == 2) {// 音频
				String path = ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
						.transferFile(ossMapList, save_path, relative_path);
				//new File(save_path).delete();
				System.out.println(path);
			} else if (file_typenum == 3) {// 视频--先给视频的图片截取一下上传，然后再上传视频文件，最后上传视频
				String path = ossStorageFactory.getInstance(ossMapList.get(0).get("oss_type").toString())
						.transferFile(ossMapList, save_path, relative_path);// 上传
				//new File(save_path).delete();
				System.out.println(path);

			}
		}
	}
}
