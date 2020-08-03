package com.mx.ai.sports.common.oss;


import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * OSS文件服务
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
public interface IOssFileService {


    /**
     * 列出某个目录下的所有文件与子文件，文件夹，子文件夹
     *
     * @param path 必填,文件在oss上的全路径
     * @return List<EkbOssObject>
     */
    List<EkbOssObject> listDir(String path);

    /**
     * 上传本地文件
     *
     * @param path 必填,文件在oss上的全路径
     * @param file 必填,本地文件
     * @return string 文件的访问路径
     */
    String uploadFile(File file, String path);

    /**
     * 上传本地文件
     *
     * @param bucketName 必填,指定bucket存放文件
     * @param path       必填,文件在oss上的全路径
     * @param file       必填,本地文件
     * @return string 文件的访问路径
     */
    String uploadFile(String bucketName, File file, String path);

    /**
     * 上传本地文件--断点续传有分片
     *
     * @param path     必填,文件在oss上的全路径
     * @param file     必填,本地文件
     * @param partSize 必填,分片大小，单位byte
     * @return string 文件的访问路径
     * @throws Throwable
     */
    String uploadFileCheckpoint(File file, String path, int partSize) throws Throwable;

    /**
     * 从byte[]构造文件
     *
     * @param path  必填,文件在oss上的全路径
     * @param bytes 必填,文件的字节数组
     * @return string 文件的访问路径
     */
    String uploadBytes(byte[] bytes, String path);


    /**
     * 从输入流读取文件
     *
     * @param path  必填,文件在oss上的目录
     * @param input 必填,文件的输入流
     * @return string 文件的访问路径
     */
    String uploadInputStream(InputStream input, String path);


    /**
     * 下载文件到本地
     *
     * @param path          必填,文件在oss上的全路径
     * @param localFilePath 必填,文件存储在本地的全路径
     * @return string 文件的访问路径
     */
    void downloadFile(String path, String localFilePath);

    /**
     * 下载文件到本地
     *
     * @param bucketName    必填,指定bucket读取文件
     * @param path          必填,文件在oss上的全路径
     * @param localFilePath 必填,文件存储在本地的全路径
     * @return string 文件的访问路径
     */
    void downloadFile(String bucketName, String path, String localFilePath);

    /**
     * 删除目录下的所有文件与子文件，文件夹，子文件夹
     *
     * @param path 必填,文件在oss上的全路径
     * @return List<SXWOssObject>
     */
    void deleteDir(String path);

}
