package com.mx.ai.sports.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转
 * @author Mengjiaxin
 * @date 2020/9/11 2:32 下午
 */
@Slf4j
@Controller("ViewController")
public class ViewController {


    @GetMapping("agreement.html")
    @ResponseBody
    public Object login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("agreement");
        return mav;
    }


}
