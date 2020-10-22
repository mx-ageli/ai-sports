package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.vo.CourseEntryVo;
import com.mx.ai.sports.system.entity.User;

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
     * 查询学生的报名的其他课程
     *
     * @param courseId 课程Id
     * @return
     */
    List<CourseStudent> findByUserNoCourseId(Long userId, Long courseId);


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

    /**
     * 保存学生的报名关系，并保存分组
     * @param courseStudent
     * @return
     */
    CourseEntryVo saveStudentAndGroup(CourseStudent courseStudent) throws AiSportsException;

    /**
     * 清除课程所有的报名学生
     * @param courseId
     * @return
     */
    int removeByCourseId(String courseId);

    /**
     * 查询当期课程的报名人数
     * @param courseId
     * @return
     */
    Long findCountByUserId(Long courseId);


    /**
     * 设置课程的计数器
     * @param courseId
     * @return
     */
    Long setCountByUserId2Redis(Long courseId, Long add);

    /**
     * 查询课程的计数器
     * @param courseId
     * @return
     */
    Long findCountByUserId2Redis(Long courseId);

    /**
     * 删除报名课程的缓存，计数器和学生列表
     * @param courseId
     * @return
     */
    void removeCountByUserId2Redis(Long courseId);

    /**
     * 设置课程学生列表
     * @param courseId
     * @return
     */
    Long setEntryStudentList2Redis(Long courseId, Long userId);

    /**
     * 查询课程学生列表
     * @param courseId
     * @return
     */
    Long findEntryStudentList2Redis(Long courseId, Long userId);

    /**
     * 删除报名课程的学生列表
     * @param courseId
     * @return
     */
    void removeEntryStudentList2Redis(Long courseId, Long userId);
}
