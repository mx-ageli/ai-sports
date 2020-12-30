package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.MenuTree;
import com.mx.ai.sports.system.entity.Menu;

import java.util.List;

/**
 * 菜单表
 * @author Mengjiaxin
 * @date 2020/12/18 下午3:24
 */
public interface IMenuService extends IService<Menu> {


    List<Menu> findUserPermissions(String userName);

    MenuTree<Menu> findUserMenus(String username);
}
