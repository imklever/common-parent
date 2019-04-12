package com.isoftstone.common.api;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author cpniuc
 * 
 * 访问地址为 http://isoftstone.oss.cn-north-1.jcloudcs.com +  key_name
 *'
 *
 * 
 */
public class S3SdkTest {

	public static void main(String[] args) throws IOException {
		final String accessKey = "664919A518F704B332306183A8861A50";//固定，不修改 可做固定参数
        final String secretKey = "CF130691582F6C184001F59CC44AA979";//固定，不修改 可做固定参数
        final String endpoint = "http://s3.cn-north-1.jcloudcs.com";//固定，不修改 可做固定参数
        String bucket_name="isoftstone";//固定，不修改 可做固定参数
        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(Protocol.HTTP);
 
        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint,bucket_name);//固定，不修改
 
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
 
        AmazonS3 s3  = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();
        
        
        
        
        List<Bucket> buckets = s3.listBuckets();
        System.out.println("Your Amazon S3 buckets are:");
        for (Bucket b : buckets) {
            System.out.println("Bucket: " + b.getName());
        }
        String key_name="video.png"; //为访问的后面路径可以加/(斜杠)   /yyyyMMdd/HHmmss文件名
        //s3.deleteObject(bucket_name, key_name);
        //上传
        long startTime=System.currentTimeMillis();
        //s3.putObject(bucket_name, "video2223.mp4",new File("D:\\test\\1111.mp4"));
       
	     
	     
	  // 初始化分片上传
	     String key = "video2224.mp4";
	     File file = new File("D:\\test\\1111.mp4");
	     long contentLength = file.length();
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
	             .withFile(file)
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
	     long endTime  =System.currentTimeMillis();
	     System.out.println((endTime-startTime)+"222222222");
	     //拷贝文件
        //s3.copyObject(bucket_name, key_name, bucket_name, "video222.png");
        //删除文件
        //s3.deleteObject(bucket_name, key_name);
        /*
        ObjectListing ol = s3.listObjects(bucket_name);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();
        //列出有多少文件
        for (S3ObjectSummary os: objects) {
        	//s3.deleteObject(bucket_name, os.getKey());
            System.out.println("os: " + os.getKey());
        }*/
        //获得访问权限
       /* AccessControlList acl = s3.getBucketAcl(bucket_name);
        if(acl!=null) {
        	 List<Grant> grants = acl.getGrantsAsList();
             for (Grant grant : grants) {
                 System.out.format("  %s: %s\n", grant.getGrantee().getIdentifier(),
                         grant.getPermission().toString());
             }
        }*/
       
        //下文件
        /**
         
         S3Object o = s3.getObject(bucket_name, key_name);
        S3ObjectInputStream s3is = o.getObjectContent();
        FileOutputStream fos = new FileOutputStream(new File(key_name));
        byte[] read_buf = new byte[1024];
        int read_len = 0;
        while ((read_len = s3is.read(read_buf)) > 0) {
            fos.write(read_buf, 0, read_len);
        }
        s3is.close();
        fos.close();
        
        */
       /*String bucket_name="test";
        if (s3.doesBucketExist(bucket_name)) {
            System.out.format("Bucket %s already exists.\n", bucket_name);
            b = getBucket(bucket_name);
        } else {
            try {
                b = s3.createBucket(bucket_name);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }*/
	}
	
}
