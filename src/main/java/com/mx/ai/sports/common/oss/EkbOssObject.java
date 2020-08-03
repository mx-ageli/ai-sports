package com.mx.ai.sports.common.oss;


import java.util.Date;

/**
 * OSS文件对象
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
public class EkbOssObject {
    /**
     * The key under which this object is stored
     */
    private String ossFilePath;

    private String accessUrl;

    private long fileSize;

    private Date lastModified;

    private OssTypeEnum ossType;

    public String getOssFilePath() {
        return ossFilePath;
    }

    public void setOssFilePath(String ossFilePath) {
        this.ossFilePath = ossFilePath;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public OssTypeEnum getOssType() {
        return ossType;
    }

    public void setOssType(OssTypeEnum ossType) {
        this.ossType = ossType;
    }

}
