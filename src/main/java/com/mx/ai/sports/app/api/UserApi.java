package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 用户相关接口
 * @author Mengjiaxin
 * @date 2020/7/14 4:40 下午
 */
@Validated
@Api(tags = "用户相关接口", protocols = "application/json")
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface UserApi {

    /**
     * 单张图片上传-返回OSS图片路径
     *
     * @param file 上传的文件
     * @return
     */
    @ApiOperation(value = "单张图片上传-返回OSS图片路径 #已实现 2020-07-14#")
    @RequestMapping(value = "/v/upload", method = RequestMethod.POST)
    AiSportsResponse<String> upload(@NotNull MultipartFile file);


}
