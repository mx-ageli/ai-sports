package com.mx.ai.sports.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云OSS服务实现
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
@Component
public class AliyunOssFileServiceImpl implements IOssFileService {

    private AliyunOssConfig aliyunOssConfig;

    Logger log = LoggerFactory.getLogger(AliyunOssFileServiceImpl.class);


    public AliyunOssFileServiceImpl(AliyunOssConfig aliyunOssConfig) {
        this.aliyunOssConfig = aliyunOssConfig;
    }

    @Override
    public List<AiSportOssObject> listDir(String path) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(aliyunOssConfig.getBucketName());
        listObjectsRequest.setPrefix(path);
        List<AiSportOssObject> list = new ArrayList<>();
        OSSClient ossClient = this.getOssClient();
        try {
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                AiSportOssObject obj = new AiSportOssObject();
                obj.setOssFilePath(objectSummary.getKey());
                obj.setOssType(OssTypeEnum.ALI_YUN);
                obj.setFileSize(objectSummary.getSize());
                obj.setAccessUrl(aliyunOssConfig.getAccessUrl() + aliyunOssConfig.getDelimiter() + objectSummary.getKey());
                list.add(obj);
            }
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }finally {
            closeOssClient(ossClient);
        }
        return list;
    }

    @Override
    public String uploadFile(File file, String path) {
        OSSClient ossClient = this.getOssClient();
        try {
            ossClient.putObject(aliyunOssConfig.getBucketName(), path, file);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }finally {
            closeOssClient(ossClient);
        }
        return aliyunOssConfig.getAccessUrl() + aliyunOssConfig.getDelimiter() + path;
    }

    @Override
    public String uploadFile(String bucketName, File file, String path) {
        OSSClient ossClient = this.getOssClient();
        try {
            ossClient.putObject(bucketName, path, file);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }finally {
            closeOssClient(ossClient);
        }
        return "http://"+bucketName+aliyunOssConfig.getAccessPoint() + aliyunOssConfig.getDelimiter() + path;
    }

    @Override
    public String uploadFileCheckpoint(File file, String path, int partSize) throws Throwable {
        OSSClient ossClient = this.getOssClient();
        try {
            // 设置断点续传请求
            UploadFileRequest uploadFileRequest = new UploadFileRequest(aliyunOssConfig.getBucketName(), path);
            // 指定上传的本地文件
            uploadFileRequest.setUploadFile(file.getAbsolutePath());
            // 指定上传并发线程数
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小
            uploadFileRequest.setPartSize(partSize);
            // 开启断点续传
            uploadFileRequest.setEnableCheckpoint(true);
            // 断点续传上传
            ossClient.uploadFile(uploadFileRequest);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        } finally {
            closeOssClient(ossClient);
        }
        return aliyunOssConfig.getAccessUrl() + aliyunOssConfig.getDelimiter() + path;
    }

    @Override
    public String uploadBytes(byte[] bytes, String path) {
        OSSClient ossClient = this.getOssClient();
        try {
            ossClient.putObject(aliyunOssConfig.getBucketName(), path, new ByteArrayInputStream(bytes));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        } finally {
            closeOssClient(ossClient);
        }
        return aliyunOssConfig.getAccessUrl() + aliyunOssConfig.getDelimiter() + path;
    }

    @Override
    public String uploadInputStream(InputStream input, String path) {
        OSSClient ossClient = this.getOssClient();
        try {
            ossClient.putObject(aliyunOssConfig.getBucketName(), path, input);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        } finally {
            closeOssClient(ossClient);
        }
        return aliyunOssConfig.getAccessUrl() + aliyunOssConfig.getDelimiter() + path;
    }

    @Override
    public void downloadFile(String path, String localFilePath) {
        OSSClient ossClient = this.getOssClient();
        try {
            ObjectMetadata ossObjectMeta = ossClient.getObject(new GetObjectRequest(aliyunOssConfig.getBucketName(), path), new File(localFilePath));
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }finally {
            closeOssClient(ossClient);
        }
    }

    @Override
    public void downloadFile(String bucketName, String path, String localFilePath) {
        OSSClient ossClient = this.getOssClient();
        try {
            ObjectMetadata ossObjectMeta = ossClient.getObject(new GetObjectRequest(bucketName, path), new File(localFilePath));
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }finally {
            closeOssClient(ossClient);
        }
    }

    @Override
    public void deleteDir(String path) {
        OSSClient ossClient = this.getOssClient();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(aliyunOssConfig.getBucketName());
            listObjectsRequest.setPrefix(path);
            ObjectListing listing = ossClient.listObjects(listObjectsRequest);
            // 删除Objects
            List<String> keys = new ArrayList<String>();
            for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
                keys.add(objectSummary.getKey());
            }
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(aliyunOssConfig.getBucketName()).withKeys(keys));
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        } finally {
            closeOssClient(ossClient);
        }
    }

    private OSSClient getOssClient() {
        //当内网OSS地址不为空的时候，优先使用
        String reallyUseEndpoint = aliyunOssConfig.getEndpoint();
        if (aliyunOssConfig.getEndpointInternal() != null && !"".equals(aliyunOssConfig.getEndpointInternal().trim())) {
            reallyUseEndpoint = aliyunOssConfig.getEndpointInternal();
        }
        return new OSSClient(reallyUseEndpoint, aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret());
    }

    private void closeOssClient(OSSClient ossClient) {
        try {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        } catch (Exception e){
            log.error("OSSClient关闭异常--" + e.getMessage(), e);
            throw new RuntimeException("文件服务暂时不能访问，稍后再试");
        }
    }
}
