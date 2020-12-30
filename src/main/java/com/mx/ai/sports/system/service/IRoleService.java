package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.Role;
import com.mx.ai.sports.system.entity.Term;
import com.mx.ai.sports.system.vo.TermVo;

import java.util.List;

/**
 * 角色表
 * @author Mengjiaxin
 * @date 2020/12/18 下午3:24
 */
public interface IRoleService extends IService<Role> {


    List<Role> findUserRole(String userName);

}
