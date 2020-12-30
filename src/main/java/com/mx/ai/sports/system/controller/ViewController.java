package com.mx.ai.sports.system.controller;

import com.mx.ai.sports.common.authentication.ShiroHelper;
import com.mx.ai.sports.common.controller.BaseController;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.utils.AiSportsUtil;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统管理所有的页面跳转
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:49
 */
@Slf4j
@Controller("systemView")
public class ViewController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ShiroHelper shiroHelper;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (AiSportsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(AiSportsUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return AiSportsUtil.view("error/403");
    }


    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentuserAuthorizationInfo();
        User user = super.getCurrentUser();
        // 获取实时的用户信息
        model.addAttribute("user", userService.findByUsername(user.getUsername()));
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(AiSportsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return AiSportsUtil.view("layout");
    }

    @RequestMapping(AiSportsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return AiSportsUtil.view("index");
    }

    /************************** 错误页面  ****************************/
    @GetMapping(AiSportsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return AiSportsUtil.view("error/404");
    }

    @GetMapping(AiSportsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return AiSportsUtil.view("error/403");
    }

    @GetMapping(AiSportsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return AiSportsUtil.view("error/500");
    }


}
