package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.TeacherRegister;
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

    /**
     * 校验用户名是否为注册老师
     * @param username
     * @return
     */
    TeacherRegister findTeacherRegisterByUsername(String username);

    /**
     * 注册老师用户
     * @param username 用户名
     * @param fullName 姓名
     * @return
     */
    Boolean registerTeacher(String username, String fullName);

    /**
     * 更新注册老师信息
     * @return
     */
    Boolean updateRegisterTeacher(TeacherRegister register);



}
