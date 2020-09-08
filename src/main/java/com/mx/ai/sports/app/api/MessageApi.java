package com.mx.ai.sports.app.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.system.vo.MessageVo;
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
 * 系统消息
 *
 * @author Mengjiaxin
 * @date 2020/9/8 6:11 下午
 */
@Validated
@Api(tags = "70-系统消息", protocols = "application/json")
@RequestMapping(value = "/api/message", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface MessageApi {

    /**
     * 查询我收到的系统消息
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-09-08# 查询我收到的系统消息")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_my_message", method = RequestMethod.POST)
    AiSportsResponse<IPage<MessageVo>> findMyMessage(@RequestBody @Valid QueryRequest query);


}
