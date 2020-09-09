package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.system.vo.ReplaceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * 版本更新接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "80-版本更新接口", protocols = "application/json")
@RequestMapping(value = "/api/replace", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ReplaceApi {

    /**
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "#已实现 2020-09-09# 获取最新的系统版本更新信息")
    @ApiImplicitParam(name = "type", value = "设备类型 1iOS 2Android", paramType = "query", dataType = "string", required = true)
    @RequestMapping(value = "/v/find_by_last", method = RequestMethod.GET)
    AiSportsResponse<ReplaceVo> findByLast(@NotNull @RequestParam("type") String type);


}