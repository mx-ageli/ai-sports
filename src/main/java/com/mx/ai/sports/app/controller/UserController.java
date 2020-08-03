package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.CommonApi;
import com.mx.ai.sports.app.api.UserApi;
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
 * 用户相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("UserApi")
public class UserController extends BaseRestController implements UserApi {

    @Autowired
    private OssUploadUtil ossUploadUtil;

    @Override
    @Log("用户图片上传")
    public AiSportsResponse<String> upload(@NotNull MultipartFile file) {
        try {
            // 上传图片, 返回OSS的图片路径
            String ossUrl = ossUploadUtil.uploadToOss(FileUtil.multipartFileToFile(file));
            return new AiSportsResponse<String>().success().data(ossUrl);
        } catch (Exception e) {
            String message = "图片上传失败,请稍后再试！";
            log.error(message, e);
            return new AiSportsResponse<String>().message(message).fail();
        }
    }

}
