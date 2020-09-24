package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.query.ExportAllQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * 导出相关接口
 * @author Mengjiaxin
 * @date 2020/9/18 9:50 上午
 */
@Validated
@Api(tags = "91-导出相关接口", protocols = "application/json")
@RequestMapping(value = "/api/export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ExportApi {

    /**
     * 导出时间段内的课程统计数据
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-09-18# 导出时间段内的课程统计数据")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "ExportAllQuery", required = true)
    @RequestMapping(value = "/export_all", method = RequestMethod.POST)
    AiSportsResponse<String> exportAll(@RequestBody @Valid ExportAllQuery query);

}