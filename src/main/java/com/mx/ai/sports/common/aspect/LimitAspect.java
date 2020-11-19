package com.mx.ai.sports.common.aspect;

import com.google.common.collect.ImmutableList;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.entity.LimitType;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wiki.xsx.core.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 接口限流
 * @author Mengjiaxin
 * @date 2020/8/3 4:36 下午
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    @Pointcut("@annotation(com.mx.ai.sports.common.annotation.Limit)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        String key;
        String ip = IpUtil.getIpAddr(request);
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        switch (limitType) {
            case IP:
                key = ip;
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        String keys = StringUtils.join(limitAnnotation.prefix() + "_", key, ip);

        Long count = RedisUtil.getNumberHandler().getLong(keys);

        if(count == null || count < limitCount ){
            count = RedisUtil.getNumberHandler().incrementLong(keys);
            if(count == 1){
                RedisUtil.getKeyHandler().expire(keys, limitPeriod, TimeUnit.SECONDS);
            }
            return point.proceed();
        } else {
            throw new AiSportsException(limitAnnotation.message(), false);
        }

    }

}
