package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.vo.UserCountVo;
import com.mx.ai.sports.system.vo.UserSmallVo;
import com.mx.ai.sports.system.vo.UserVo;

import java.util.Date;
import java.util.List;

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
    Boolean registerTeacher(String username, String fullName, Long schoolId);

    /**
     * 更新注册老师信息
     * @return
     */
    Boolean updateRegisterTeacher(TeacherRegister register);

    /**
     * 根据学号查询
     * @param sno
     * @return
     */
    User findBySno(String sno);

    /**
     * 检查用户是否已经注册，如果没有注册默认系统注册
     * @param mobile 手机号
     * @return
     */
    User checkUserAndRegister(String mobile);

    /**
     * 根据学校Id查询学校信息
     * @param schoolId
     * @return
     */
    School findSchoolById(Long schoolId);


    /**
     *
     * @param userId
     * @return
     */
    UserVo findVoById(Long userId);


    /**
     * 根据班级Id和分页查询
     * @param query
     * @return
     */
    IPage<UserSmallVo> findByClassesId(ClassesQuery query);

    /**
     * 查询学校下面所有学生Id和设备Id
     * @param schoolId
     * @return
     */
    List<User> findStudentBySchoolId(Long schoolId);

    /**
     *
     * @param value
     * @return
     */
    List<User> findByRole(Long value);

    /**
     * 每日用户活跃数量
     * @param startTime
     * @param endTime
     * @return
     */
    List<UserCountVo> findActiveUserCount(Date startTime, Date endTime);

    /**
     * 每日用户活跃数量
     * @param startTime
     * @param endTime
     * @return
     */
    List<UserCountVo> findAddUserCount(Date startTime, Date endTime);
}
