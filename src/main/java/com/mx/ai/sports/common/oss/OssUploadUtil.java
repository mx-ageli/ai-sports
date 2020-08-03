package com.mx.ai.sports.common.oss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 文件上传到OSS工具类
 *
 * @author Mengjiaxin
 * @date 2019-08-15
 */
@Component
@ConfigurationProperties(prefix = "prefix")
public class OssUploadUtil {

    @Autowired
    private OssConfigProperties ossConfig;

    @Autowired
    private IOssFileService ossService;

    /**
     * 上传文件到oss环境
     * @param file
     * @return
     */
    public String uploadToOss(File file) {
        String ossUrl = ossService.uploadFile(file, makeOssPath(ossConfig, file.getName()));
        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ossUrl;
    }

    private String makeOssPath(OssConfigProperties ossConfig, String fileName) {
        return ossConfig.getRootFolder() + ossConfig.getDelimiter()
                + System.currentTimeMillis() + ossConfig.getDelimiter()
                + fileName;
    }


}
