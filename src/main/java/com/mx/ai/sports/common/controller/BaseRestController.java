package com.mx.ai.sports.common.controller;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 通用的RestController
 * @author Mengjiaxin
 * @date 2019/9/9 6:20 下午
 */
public class BaseRestController<T> {

    @Autowired
    private IUserService userService;

    protected UserSimple getCurrentUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();

        final String requestHeader = request.getHeader(AiSportsConstant.AUTH_HEADER);

        if(StringUtils.isBlank(requestHeader)){
            return null;
        }

        if(JwtTokenUtil.isTokenExpired(requestHeader)){
            return null;
        }

        String simple = JwtTokenUtil.getUsernameFromToken(requestHeader);

        return JSON.parseObject(simple, UserSimple.class);
    }

    protected HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(servletRequestAttributes).getRequest();
    }

    protected Long getCurrentUserId(){
        UserSimple userSimple =  getCurrentUser();
        if(userSimple == null){
            return null;
        }
        return getCurrentUser().getUserId();
    }

    protected User getUser(){
        Long userId = getCurrentUserId();
        if(userId == null){
            return null;
        }
        return userService.getById(userId);
    }

}
