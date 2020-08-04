package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.User;

/**
 * 用户Service
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:58
 */
public interface IUserService extends IService<User> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    User findByUsername(String username);

}
