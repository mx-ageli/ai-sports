package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.RedisConnectException;
import com.mx.ai.sports.monitor.entity.RedisInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 项目监控相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "99-项目全局监控接口（后台）", protocols = "application/json")
@RequestMapping(value = "/api/monitor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface MonitorApi {

    /**
     * 查看Redis当前的实时状况
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-06# 查看Redis当前的基础信息的实时状况")
    @RequestMapping(value = "/v/redis_info", method = RequestMethod.GET)
    AiSportsResponse<List<RedisInfo>> redisInfo() throws RedisConnectException;

    /**
     * 查看Redis当前的内存实时状况
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-06# 查看Redis当前的内存实时状况")
    @RequestMapping(value = "/v/memory_info", method = RequestMethod.GET)
    AiSportsResponse<Map<String, Object>> memoryInfo() throws RedisConnectException;

    /**
     * 查看Redis当前的key数量
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-06# 查看Redis当前的key数量")
    @RequestMapping(value = "/v/key_size", method = RequestMethod.GET)
    AiSportsResponse<Map<String, Object>> keySize() throws RedisConnectException;

}