package com.mx.ai.sports.common.interceptor;

import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.JwtTokenUtil;
import com.mx.ai.sports.common.utils.RenderUtil;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Rest Api接口鉴权
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:23
 */
public class RestApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }
        return check(request, response);
    }

    private boolean check(HttpServletRequest request, HttpServletResponse response) throws AiSportsException {

        for (String authPath : AiSportsConstant.AUTH_PATH) {
            if (request.getServletPath().contains(authPath)) {
                return true;
            }
        }

        final String requestHeader = request.getHeader(AiSportsConstant.AUTH_HEADER);
        if (StringUtils.isNotBlank(requestHeader)) {
            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = JwtTokenUtil.isTokenExpired(requestHeader);
                if (flag) {
                    RenderUtil.renderJson(response, new AiSportsResponse().code(HttpStatus.UNAUTHORIZED).message("登录认证过期,需要重新登录!"));
                    return false;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new AiSportsResponse().code(HttpStatus.UNAUTHORIZED).message("token解析失败"));
                return false;
            }
        } else {
            //header不能为空
            RenderUtil.renderJson(response, new AiSportsResponse().code(HttpStatus.UNAUTHORIZED).message("header信息不能为空, 请header中传入Authorization参数, 格式:eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNTY0NzM2MDkyLCJpYXQiOjE1NjQxMzEyOTJ9.UOZka6D2Gyuf4jK4WKzlCOGIdCuyUEGqJ5U1yF8jTCQO_mX8sM5NSdjag0AdRK7hhlGYGd2qJP9bzkc3uXU0YA"));
            return false;
        }
        return true;
    }

}
