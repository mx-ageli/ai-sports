package com.mx.ai.sports.common.oss;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OSS自定义配置
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "oss")
public class OssConfigProperties extends OssConfig {
    private static final long serialVersionUID = 544675343383254485L;

    /**
     * 是否开启OSS, 默认为true
     */
    private boolean enabled = true;


    private AliyunOssConfig aliyun;

    private QiniuOssConfig qiniu;



    @Data
    @ToString
    public static class AliyunOssConfig {

        /**
         * 是否开启阿里云OSS
         */
        private boolean enabled = false;

        /**
         * aliyun oss 内网访问路径，当不能使用OSS内网环境的时候，请配置为外网endpoint或者不填写
         */
        private String endpointInternal;

        /**
         * aliyun oss 文件操作地址
         */
        private String endpoint;

        /**
         * aliyun oss accessKeyId
         */
        private String accessKeyId;

        /**
         * aliyun oss accessKeySecret
         */
        private String accessKeySecret;

        /**
         * aliyun oss bucketName
         */
        private String bucketName;

        /**
         * 区域ID
         */
        private String regionId;

        /**
         * 角色描述符
         */
        private String roleArn;

        /**
         * STS授权过期时间，单位秒
         */
        private Long expireSecond;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }


        public String getBucketName() {
            return bucketName;
        }
    }

    @Data
    @ToString
    public static class QiniuOssConfig {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public AliyunOssConfig getAliyun() {
        return aliyun;
    }

    public QiniuOssConfig getQiniu() {
        return qiniu;
    }
}
