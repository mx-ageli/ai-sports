package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;

import java.sql.Time;
import java.util.List;

/**
 * 课程Service
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ICourseService extends IService<Course> {

    /**
     * 通过课程名称查找课程, 如果courseId不为空就排除
     *
     * @param courseId 排除的课程Id
     * @param courseName 课程名称
     * @return
     */
    Course findByCourseName(Long courseId, String courseName);

}
