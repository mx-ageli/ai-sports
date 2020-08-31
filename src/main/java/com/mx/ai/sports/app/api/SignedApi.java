package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.query.SignedAddVo;
import com.mx.ai.sports.course.vo.RunRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 打卡签到相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 3:53 下午
 */
@Validated
@Api(tags = "40-打卡签到相关接口", protocols = "application/json")
@RequestMapping(value = "/api/signed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SignedApi {

    // TODO 查询打卡信息


    // TODO 打卡

    /**
     * 打卡
     *
     * @param signedAddVo
     * @return
     */
    @ApiOperation(value = "#已实现 图# 打卡")
    @RequestMapping(value = "/signed", method = RequestMethod.POST)
    AiSportsResponse<Boolean> signed(@RequestBody @Valid SignedAddVo signedAddVo);

}
