package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.system.entity.Menu;
import com.mx.ai.sports.system.entity.Role;

import java.util.List;

/**
 * 菜单mapper
 * @author Mengjiaxin
 * @date 2020/8/4 11:19 上午
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findUserPermissions(String username);

    List<Menu> findUserMenus(String username);
}
