package com.isoftstone.common.backup.service.jdoss.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

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
import com.isoftstone.common.backup.service.jdoss.OssStorageService;
@Service
public class OssStorageServiceImpl implements OssStorageService{
	public static void main(String[] args) {
		AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
		s3.deleteObject(bucket_name, "20190109/c642a339d8a9709879abd54bccc3a051.jpg");
	}
	static String accessKey = "664919A518F704B332306183A8861A50";//固定，不修改 可做固定参数
    static String secretKey = "CF130691582F6C184001F59CC44AA979";//固定，不修改 可做固定参数
    static String endpoint = "http://s3.cn-north-1.jcloudcs.com";//固定，不修改 可做固定参数
    static String bucket_name="isoftstone";//固定，不修改 可做固定参数
    public final static ConcurrentHashMap<String, AmazonS3> S3_HASH_MAP = new ConcurrentHashMap<String, AmazonS3>();
	@Override
	public Object delOss(Map<String, Object> datas) {
		if(datas != null && datas.containsKey("delDataList")) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) datas.get("delDataList");
			AmazonS3 s3 = getJdAmazonS3(endpoint,bucket_name,accessKey,secretKey);
			for (Map<String, Object> map : list) {
				String key_name=map.get("relative_path").toString();
				//删除文件
				try {
					  s3.deleteObject(bucket_name, key_name);
					  if(3==(int)map.get("file_typenum")) {
						  String pngpath=key_name.substring(0,key_name.lastIndexOf("."))+".jpg";
						  s3.deleteObject(bucket_name, pngpath);
					  }
				} catch (Exception e) {
					System.out.println("----删除文件失败-");
				}
		      
			}
		}
		return null;
	}
	private static AmazonS3 getJdAmazonS3(String endpoint2, String bucket_name2, String accessKey2, String secretKey2) {
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

}
