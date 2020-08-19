package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseStudent;

import java.util.List;

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

}
