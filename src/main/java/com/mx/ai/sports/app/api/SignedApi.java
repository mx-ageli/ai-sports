package com.mx.ai.sports.app.api;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 打卡签到相关接口
 * @author Mengjiaxin
 * @date 2020/8/3 3:53 下午
 */
@Validated
@Api(tags = "40-打卡签到相关接口", protocols = "application/json")
@RequestMapping(value = "/api/signed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SignedApi {

    // TODO 查询打卡信息


    // TODO 打卡


}
