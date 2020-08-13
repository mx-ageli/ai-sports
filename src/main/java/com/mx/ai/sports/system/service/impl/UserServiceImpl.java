package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.oss.AliyunOssConfig;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.mapper.SchoolMapper;
import com.mx.ai.sports.system.mapper.TeacherRegisterMapper;
import com.mx.ai.sports.system.mapper.UserMapper;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.UserSmallVo;
import com.mx.ai.sports.system.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_AVATAR;
import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_SCHOOL_ID;

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

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private AliyunOssConfig aliyunOssConfig;

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

    @Override
    public User findBySno(String sno) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getSno, sno));
    }


    /**
     * 检查用户是否已经注册，如果没有注册默认系统注册
     *
     * @param mobile
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User checkUserAndRegister(String mobile) {
        // 先通过手机号查询用户信息
        User user = this.findByUsername(mobile);
        // 用户还没有注册，系统给默认注册
        if (user == null) {
            user = new User();
            user.setUsername(mobile);
            user.setCreateTime(new Date());
            user.setModifyTime(new Date());
            user.setLastLoginTime(new Date());
            user.setSchoolId(DEFAULT_SCHOOL_ID);
            user.setAvatar(aliyunOssConfig.getAccessUrl() + DEFAULT_AVATAR);

            // 检查这个用户是不是老师
            TeacherRegister teacherRegister = this.findTeacherRegisterByUsername(mobile);
            // 存在说明为老师
            if (teacherRegister != null) {
                // 使用老师登记的姓名
                user.setFullName(teacherRegister.getFullName());
                user.setRoleId(RoleEnum.TEACHER.value());

                teacherRegister.setIsRegister(Boolean.TRUE);
                this.updateRegisterTeacher(teacherRegister);
            } else {
                user.setRoleId(RoleEnum.STUDENT.value());
            }

            this.save(user);
        }
        return user;
    }

    @Override
    public School findSchoolById(Long schoolId) {
        return schoolMapper.selectById(schoolId);
    }

    @Override
    public UserVo findVoById(Long userId) {

        return baseMapper.findVoById(userId);
    }

    @Override
    public IPage<UserSmallVo> findByClassesId(ClassesQuery query) {
        Page<User> page = new Page<>(query.getRequest().getPageNum(), query.getRequest().getPageSize());
        return baseMapper.findByClassesId(page, query.getClassesId());
    }
}
