package com.mx.ai.sports.app.api;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 小组相关接口
 * @author Mengjiaxin
 * @date 2020/9/24 2:05 下午
 */
@Validated
@Api(tags = "21-小组相关接口", protocols = "application/json")
@RequestMapping(value = "/api/group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface GroupApi {

    // TODO 查询一个课程下面的小组

    // TODO 查询一个老师下面的小组

    // TODO 查询一个小组内的学生列表



}