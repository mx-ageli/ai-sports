package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * @param courseId
     * @param courseName
     * @return
     */
    Course findByCourseName(Long courseId, String courseName);

    /**
     * @param page
     * @return
     */
    IPage<CourseVo> findAll(Page<StudentCourseVo> page, int week, Long userId);

    /**
     * @param courseId
     * @return
     */
    CourseVo findById(int week, Long courseId);

    /**
     * @param page
     * @param userId
     * @return
     */
    IPage<CourseVo> findMyEntry(Page<CourseVo> page, int week, Long userId);
}