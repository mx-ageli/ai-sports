package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.oss.AliyunOssConfig;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.mapper.ClassesMapper;
import com.mx.ai.sports.system.mapper.SchoolMapper;
import com.mx.ai.sports.system.mapper.TeacherRegisterMapper;
import com.mx.ai.sports.system.mapper.UserMapper;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.mx.ai.sports.common.entity.AiSportsConstant.DEFAULT_AVATAR;

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

    @Autowired
    private ClassesMapper classesMapper;

    @Autowired
    private IRecordStudentService recordStudentService;

    @Override
    public User findByUsername(String username) {

        return this.baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public TeacherRegister findTeacherRegisterByUsername(String username) {

        return teacherRegisterMapper.selectOne(new LambdaQueryWrapper<TeacherRegister>().eq(TeacherRegister::getUsername, username));
    }

    @Override
    public Boolean registerTeacher(String username, String fullName, Long schoolId) {
        TeacherRegister register = new TeacherRegister();
        register.setUsername(username);
        register.setFullName(fullName);
        register.setIsRegister(Boolean.FALSE);
        register.setSchoolId(schoolId);

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
            user.setAvatar(aliyunOssConfig.getAccessUrl() + DEFAULT_AVATAR);

            // 检查这个用户是不是老师
            TeacherRegister teacherRegister = this.findTeacherRegisterByUsername(mobile);
            // 存在说明为老师
            if (teacherRegister != null) {
                // 使用老师登记的姓名
                user.setFullName(teacherRegister.getFullName());
                user.setRoleId(RoleEnum.TEACHER.value());
                user.setSchoolId(teacherRegister.getSchoolId());
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

        UserVo userVo = baseMapper.findVoById(userId);
        if(Objects.equals(userVo.getRoleId(), RoleEnum.STUDENT.value()) && userVo.getClassesId() != null){
            // 查询学生的主课信息
            SubjectStudentVo subjectStudentVo = this.baseMapper.findSubjectByUserId(userId);
            userVo.setSubjectStudent(subjectStudentVo);

            ClassesSmallVo classesSmallVo = classesMapper.findById(userVo.getClassesId());

            ClassesVo classesVo = new ClassesVo(classesSmallVo);
            // 班级的任课老师直接使用学生的任课老师
            classesVo.setUserId(subjectStudentVo.getTeacherId());
            classesVo.setTeacherName(subjectStudentVo.getTeacherName());
            userVo.setClasses(classesVo);

            // 查询学生的课程次数和合格次数
            List<RecordStudent> recordStudentList = recordStudentService.list(new LambdaQueryWrapper<RecordStudent>().eq(RecordStudent::getIsAbsent, false).eq(RecordStudent::getUserId, userId));
            if(!CollectionUtils.isEmpty(recordStudentList)){
                userVo.setCourseCount((long) recordStudentList.size());
                // 没有缺席、没有迟到、没有早退、成绩合格才算数量
                userVo.setPassCount(recordStudentList.stream()
                        .filter(e -> !e.getIsAbsent() && !e.getIsLate() && !e.getIsGone() && e.getIsPass()).count());
            }
        }
        return userVo;
    }

    @Override
    public IPage<UserSmallVo> findByClassesId(ClassesQuery query) {
        Page<User> page = new Page<>(query.getRequest().getPageNum(), query.getRequest().getPageSize());
        return baseMapper.findByClassesId(page, query.getClassesId());
    }

    @Override
    public List<User> findStudentBySchoolId(Long schoolId) {
        return baseMapper.findStudentBySchoolId(schoolId);
    }

    @Override
    public List<User> findByRole(Long value) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getRoleId, value));
    }

    @Override
    public List<UserCountVo> findActiveUserCount(Date startTime, Date endTime) {
        return this.baseMapper.findActiveUserCount(startTime, endTime);
    }

    @Override
    public List<UserCountVo> findAddUserCount(Date startTime, Date endTime) {
        return this.baseMapper.findAddUserCount(startTime, endTime);
    }
}
