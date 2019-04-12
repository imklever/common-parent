package com.isoftstone.common.api.service.oss.Impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.isoftstone.common.api.service.oss.OssStorageService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JDOssStorageServiceImpl implements OssStorageService{
	String accessKey = "664919A518F704B332306183A8861A50";//固定，不修改 可做固定参数
    String secretKey = "CF130691582F6C184001F59CC44AA979";//固定，不修改 可做固定参数
    String endpoint = "http://s3.cn-north-1.jcloudcs.com";//固定，不修改 可做固定参数
    String bucket_name="isoftstone";//固定，不修改 可做固定参数
    public final static ConcurrentHashMap<String, AmazonS3> S3_HASH_MAP 
    = new ConcurrentHashMap<String, AmazonS3>();
	
	public String transferFile(List<Map<String, Object>> mapList, File localFilePath, String remoteRelativePath) {
		if(mapList!=null && mapList.size()>0){
        	for(Map<String, Object> map : mapList){
        		if(map.containsKey("access_key")){
        			accessKey=(String) map.get("access_key");
        		}
        		if(map.containsKey("secret_key")){
        			secretKey=(String) map.get("secret_key");
        		}
        		if(map.containsKey("endpoint")){
        			endpoint =(String) map.get("endpoint");
        		}
        		if(map.containsKey("bucket_name")){
        		 	bucket_name=(String) map.get("bucket_name");
        		}   
        	}
        }
                
		AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
		s3.putObject(bucket_name, remoteRelativePath, localFilePath);
		
		return endpoint+"/"+bucket_name + "/" + remoteRelativePath;
	}
	@Override
	public String transferFile(List<Map<String, Object>> mapList,String localFilePath,String remoteRelativePath) {
        if(mapList!=null && mapList.size()>0){
        	for(Map<String, Object> map : mapList){
        		if(map.containsKey("access_key")){
        			accessKey=(String) map.get("access_key");
        		}
        		if(map.containsKey("secret_key")){
        			secretKey=(String) map.get("secret_key");
        		}
        		if(map.containsKey("endpoint")){
        			endpoint =(String) map.get("endpoint");
        		}
        		if(map.containsKey("bucket_name")){
        		 	bucket_name=(String) map.get("bucket_name");
        		}   
        	}
        }
                
        File localFile = new File(localFilePath);
		AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
		//s3.putObject(bucket_name, remoteRelativePath, localFile);
		 String key = remoteRelativePath;
	
	     //File file = new File("D:\\test\\1111.mp4");
	  // 初始化分片上传
	     long contentLength = localFile.length();
	     long partSize = 5 * 1024 * 1024; // 设置每个分片大小为5MB.
	  // 创建对象的Etag列表，并取回每个分片的Etag。
	     List<PartETag> partETags = new ArrayList<PartETag>();
	  // 初始化分片上传
	     InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket_name, key);
	     InitiateMultipartUploadResult initResponse = s3.initiateMultipartUpload(initRequest);
	  // 上传分片
	     long filePosition = 0;
	     for (int i = 1; filePosition < contentLength; i++) {
	         partSize = Math.min(partSize, (contentLength - filePosition));
	         UploadPartRequest uploadRequest = new UploadPartRequest()
	             .withBucketName(bucket_name)
	             .withKey(key)
	             .withUploadId(initResponse.getUploadId())
	             .withPartNumber(i)
	             .withFileOffset(filePosition)
	             .withFile(localFile)
	             .withPartSize(partSize);
	             // 上传分片并将返回的Etag加入列表中
	         UploadPartResult uploadResult = s3.uploadPart(uploadRequest);
	         partETags.add(uploadResult.getPartETag());
	         filePosition += partSize;
	     }
	  // 完成分片上传
	     CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket_name, key,
	     initResponse.getUploadId(), partETags);
	     s3.completeMultipartUpload(compRequest);
	     System.out.println("分布式上传完成！"+remoteRelativePath);
		return endpoint+"/"+bucket_name + "/" + remoteRelativePath;
	}
	@Override
	public String deleteFile(List<Map<String, Object>> mapList,String key_name) {
        if(mapList!=null && mapList.size()>0){
        	for(Map<String, Object> map : mapList){
        		if(map.containsKey("access_key")){
        			accessKey=(String) map.get("access_key");
        		}
        		if(map.containsKey("secret_key")){
        			secretKey=(String) map.get("secret_key");
        		}
        		if(map.containsKey("endpoint")){
        			endpoint =(String) map.get("endpoint");
        		}
        		if(map.containsKey("bucket_name")){
        		 	bucket_name=(String) map.get("bucket_name");
        		}   
        	}
        }
                
		AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
		 //删除文件
        s3.deleteObject(bucket_name, key_name);
		return "云端素材删除成功";
	}
	private AmazonS3 getJdAmazonS3(String endpoint,String bucket_name,String accessKey,String secretKey)
	{
		AmazonS3 s3 =null;
		if(S3_HASH_MAP.contains(accessKey)) {
			s3=S3_HASH_MAP.get(accessKey);
			
		}else {
			System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
	        ClientConfiguration config = new ClientConfiguration();
	        config.setProtocol(Protocol.HTTP);
	 
	        AwsClientBuilder.EndpointConfiguration endpointConfig =
	                new AwsClientBuilder.EndpointConfiguration(endpoint,bucket_name);//固定，不修改
	 
	        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
	        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
	         s3  = AmazonS3Client.builder()
	                .withEndpointConfiguration(endpointConfig)
	                .withClientConfiguration(config)
	                .withCredentials(awsCredentialsProvider)
	                .disableChunkedEncoding()
	                .withPathStyleAccessEnabled(true)
	                .build();
	         S3_HASH_MAP.put(accessKey, s3);
		}
        return s3;
	}
	@Override
	public String transferFileJust(List<Map<String, Object>> mapList, String localFilePath, String remoteRelativePath) {
		if(mapList!=null && mapList.size()>0){
        	for(Map<String, Object> map : mapList){
        		if(map.containsKey("access_key")){
        			accessKey=(String) map.get("access_key");
        		}
        		if(map.containsKey("secret_key")){
        			secretKey=(String) map.get("secret_key");
        		}
        		if(map.containsKey("endpoint")){
        			endpoint =(String) map.get("endpoint");
        		}
        		if(map.containsKey("bucket_name")){
        		 	bucket_name=(String) map.get("bucket_name");
        		}   
        	}
        }
		return endpoint+"/"+bucket_name + "/" + remoteRelativePath;
	}
	@Override
	public void copyFile(List<Map<String, Object>> mapList, String s, String imgrelativePath) {
		if(mapList!=null && mapList.size()>0){
        	for(Map<String, Object> map : mapList){
        		if(map.containsKey("access_key")){
        			accessKey=(String) map.get("access_key");
        		}
        		if(map.containsKey("secret_key")){
        			secretKey=(String) map.get("secret_key");
        		}
        		if(map.containsKey("endpoint")){
        			endpoint =(String) map.get("endpoint");
        		}
        		if(map.containsKey("bucket_name")){
        		 	bucket_name=(String) map.get("bucket_name");
        		}   
        	}
        }
                
		AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
		s3.copyObject(bucket_name, s, bucket_name, imgrelativePath);
	}

   
}
