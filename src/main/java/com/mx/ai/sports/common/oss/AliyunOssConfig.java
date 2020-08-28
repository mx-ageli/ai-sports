package com.mx.ai.sports.common.oss;

import lombok.Builder;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS服务配置
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
@Component
public class AliyunOssConfig extends OssConfig {
    private static final long serialVersionUID = 1L;

    /**
     * 默认的aliyun oss 操作地址不能随便修改
     */
    private String endpoint = "http://oss-cn-chengdu.aliyuncs.com";

    /**
     * 默认的aliyun oss 文件读取地址不能随便修改
     */
    private final String accessPoint = ".oss-cn-chengdu.aliyuncs.com";

    private String accessKeyId = "LTAI4FqD1WE7i5mPYsQ7UMrJ";

    private String accessKeySecret = "6sKjUKKQ8tNp12qDukJZuCpjcpU87P";

    private String bucketName = "ai-sports";

    /**
     * aliyun oss 内网访问路径，当不能使用OSS内网环境的时候，请配置为外网endpoint或者不填写
     */
    private String endpointInternal;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 角色描述符
     */
    private String roleArn;

    /**
     * STS授权过期时间，单位秒，默认3600秒
     */
    private Long expireSecond = 3600L;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public Long getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(Long expireSecond) {
        this.expireSecond = expireSecond;
    }

    public String getEndpointInternal() {
        return endpointInternal;
    }

    public void setEndpointInternal(String endpointInternal) {
        this.endpointInternal = endpointInternal;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessPoint() {
        return accessPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessUrl() {
        return "http://" + this.bucketName + accessPoint;
    }
}
