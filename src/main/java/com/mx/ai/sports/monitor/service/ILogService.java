package com.mx.ai.sports.monitor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mx.ai.sports.monitor.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * 系统日志Service
 * @author Mengjiaxin
 * @date 2019-08-20 19:54
 */
public interface ILogService extends IService<Log> {

    /**
     * 异步保存操作日志
     *
     * @param point 切点
     * @param log   日志
     * @throws JsonProcessingException 异常
     */
    @Async("ekbAsyncThreadPool")
    void saveLog(ProceedingJoinPoint point, Log log, Object result) throws JsonProcessingException;
}
