package com.mx.ai.sports.common.aspect;

import com.mx.ai.sports.user.dto.UserSimple;
import com.mx.ai.sports.common.properties.AiSportsProperties;
import com.mx.ai.sports.common.utils.AiSportsUtil;
import com.mx.ai.sports.common.utils.HttpContextUtil;
import com.mx.ai.sports.common.utils.IpUtil;
import com.mx.ai.sports.monitor.entity.Log;
import com.mx.ai.sports.monitor.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:10
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private AiSportsProperties aiSportsProperties;

    @Autowired
    private ILogService logService;

    @Pointcut("@annotation(com.mx.ai.sports.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP地址
        String ip = IpUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (aiSportsProperties.isOpenAopLog()) {
            // 保存日志
            UserSimple user = AiSportsUtil.getCurrentUser();
            Log log = new Log();
            if (user != null) {
                log.setUsername(user.getUsername());
            }
            log.setIp(ip);
            log.setTime(time);
            logService.saveLog(point, log, result);
        }
        return result;
    }
}
