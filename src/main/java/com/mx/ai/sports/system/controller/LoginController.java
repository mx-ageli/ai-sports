package com.mx.ai.sports.system.controller;

import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.controller.BaseController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.Md5Util;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * 登录Controller
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:50
 */
@Slf4j
@Validated
@RestController
public class LoginController extends BaseController {

    @Autowired
    private IUserService userService;

    @PostMapping("login")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public AiSportsResponse<String> login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            boolean rememberMe, HttpServletRequest request) throws AiSportsException {
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            super.login(token);
            User user = userService.findByUsername(username);
            if (user == null) {
                log.error("用户名:{} 无效, 查询不到用户数据", username);
                throw new AiSportsException("用户名无效!");
            }
            return new AiSportsResponse<String>().success();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            throw new AiSportsException(e.getMessage());
        } catch (AuthenticationException e) {
            throw new AiSportsException("用户名或密码错误！");
        }
    }



}
