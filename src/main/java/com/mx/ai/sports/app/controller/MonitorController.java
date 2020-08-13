package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.MonitorApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.RedisConnectException;
import com.mx.ai.sports.monitor.entity.RedisInfo;
import com.mx.ai.sports.monitor.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目监控相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:34 下午
 */
@Slf4j
@RestController("MonitorApi")
public class MonitorController extends BaseRestController implements MonitorApi {

    @Autowired
    private IRedisService redisService;

    @Override
    public AiSportsResponse<List<RedisInfo>> redisInfo() throws RedisConnectException {
        return new AiSportsResponse<List<RedisInfo>>().success().data(redisService.getRedisInfo());
    }

    @Override
    public AiSportsResponse<Map<String, Object>> memoryInfo() throws RedisConnectException {
        return new AiSportsResponse<Map<String, Object>>().success().data(redisService.getMemoryInfo());
    }

    @Override
    public AiSportsResponse<Map<String, Object>> keySize() throws RedisConnectException {
        return new AiSportsResponse<Map<String, Object>>().success().data(redisService.getKeysSize());
    }


}
