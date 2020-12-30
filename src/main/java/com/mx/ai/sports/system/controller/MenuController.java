package com.mx.ai.sports.system.controller;


import com.mx.ai.sports.common.controller.BaseController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.MenuTree;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.system.entity.Menu;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 菜单管理Controller
 * @author Mengjiaxin
 * @date 2019-08-20 19:50
 */
@Slf4j
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @GetMapping("{username}")
    public AiSportsResponse<MenuTree<Menu>> getUserMenus(@NotBlank(message = "{required}") @PathVariable String username) throws AiSportsException {
        User currentUser = getCurrentUser();
        if (!StringUtils.equalsIgnoreCase(username, currentUser.getUsername())) {
            throw new AiSportsException("您无权获取别人的菜单");
        }
        MenuTree<Menu> userMenus = this.menuService.findUserMenus(username);
        return new AiSportsResponse<MenuTree<Menu>>().data(userMenus);
    }



}
