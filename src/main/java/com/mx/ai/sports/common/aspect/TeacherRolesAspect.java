package com.mx.ai.sports.common.aspect;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.HttpContextUtil;
import com.mx.ai.sports.common.utils.JwtTokenUtil;
import com.mx.ai.sports.system.vo.UserSimple;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * AOP 记录用户操作日志
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:10
 */
@Slf4j
@Aspect
@Component
public class TeacherRolesAspect {


    @Pointcut("@annotation(com.mx.ai.sports.common.annotation.TeacherRole)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String requestHeader = request.getHeader(AiSportsConstant.AUTH_HEADER);

        String simple = JwtTokenUtil.getUsernameFromToken(requestHeader);
        UserSimple userSimple = JSON.parseObject(simple, UserSimple.class);
        // 请求的方法参数值
        if(!Objects.equals(userSimple.getRoleId(), RoleEnum.TEACHER.value())){
            throw new AiSportsException("当前接口只能是老师才能访问！");
        }

        return point.proceed();
    }
}
