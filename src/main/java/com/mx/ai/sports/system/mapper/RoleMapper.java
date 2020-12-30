package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.system.entity.Role;

import java.util.List;

/**
 * 角色mapper
 * @author Mengjiaxin
 * @date 2020/8/4 11:19 上午
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findUserRole(String username);

}
