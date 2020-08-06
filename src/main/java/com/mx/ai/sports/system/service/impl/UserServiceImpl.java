package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.mapper.TeacherRegisterMapper;
import com.mx.ai.sports.system.mapper.UserMapper;
import com.mx.ai.sports.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Service
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:58
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private TeacherRegisterMapper teacherRegisterMapper;

    @Override
    public User findByUsername(String username) {

        return this.baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public TeacherRegister findTeacherRegisterByUsername(String username) {

        return teacherRegisterMapper.selectOne(new LambdaQueryWrapper<TeacherRegister>().eq(TeacherRegister::getUsername, username));
    }

    @Override
    public Boolean registerTeacher(String username, String fullName) {
        TeacherRegister register = new TeacherRegister();
        register.setUsername(username);
        register.setFullName(fullName);
        register.setIsRegister(Boolean.FALSE);

        return teacherRegisterMapper.insert(register) > 0;
    }

    @Override
    public Boolean updateRegisterTeacher(TeacherRegister register) {
        return teacherRegisterMapper.updateById(register) > 0;
    }

}
