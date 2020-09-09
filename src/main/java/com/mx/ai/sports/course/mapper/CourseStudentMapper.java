package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.system.entity.User;

import java.util.List;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {

    List<User> findUserByCourseId(Long courseId);
}