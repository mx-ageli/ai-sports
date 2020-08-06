package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.system.vo.TermVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 学期相关接口
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "30-学期相关接口", protocols = "application/json")
@RequestMapping(value = "/api/term", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface TermApi {

    /**
     * 查询所有的学期列表
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-06# 查询所有的学期列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    AiSportsResponse<List<TermVo>> list();





}