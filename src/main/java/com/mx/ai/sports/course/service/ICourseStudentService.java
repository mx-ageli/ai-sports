package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.dto.CourseStudentCountDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.system.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程报名的学生Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ICourseStudentService extends IService<CourseStudent> {

    /**
     * 根据课程Id查询所有报名的学生Id
     *
     * @param courseId 课程Id
     * @return
     */
    List<Long> findByCourseId(Long courseId);

    /**
     * 根据课程Id查询所有报名的学生
     *
     * @param courseId 课程Id
     * @return
     */
    List<User> findUserByCourseId(Long courseId);

    /**
     * 查询我的报名的课程
     *
     * @param userId 用户Id
     * @return
     */
    List<Long> findByUserId(Long userId);

    /**
     * 查询学生的报名
     *
     * @param courseId 课程Id
     * @return
     */
    CourseStudent findByUserCourseId(Long userId, Long courseId);


    /**
     * 删除
     * @param userId
     * @param courseId
     * @return
     */
    Boolean remove(Long userId, Long courseId);

    /**
     * 统计当前报名的学生数量
     * @return
     */
    Map<Long, Long> findCourseStudentCount();

}
