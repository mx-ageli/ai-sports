package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.UploadApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.oss.OssUploadUtil;
import com.mx.ai.sports.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


/**
 * 基础上传服务
 * @author Mengjiaxin
 * @date 2020/8/6 10:58 上午
 */
@Slf4j
@RestController("UploadApi")
public class UploadController extends BaseRestController implements UploadApi {

    @Autowired
    private OssUploadUtil ossUploadUtil;

    @Override
    @Log("用户上传文件")
    public AiSportsResponse<String> upload(@NotNull MultipartFile file) {
        try {
            // 上传文件, 返回OSS的文件路径
            String ossUrl = ossUploadUtil.uploadToOss(FileUtil.multipartFileToFile(file));
            return new AiSportsResponse<String>().success().data(ossUrl);
        } catch (Exception e) {
            String message = "文件上传失败,请稍后再试！";
            log.error(message, e);
            return new AiSportsResponse<String>().message(message).fail();
        }
    }

}
