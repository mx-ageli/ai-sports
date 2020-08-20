package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.mapper.CourseMapper;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public Course findByCourseName(Long courseId, String courseName) {
        return baseMapper.findByCourseName(courseId, courseName);
    }

    @Override
    public IPage<CourseVo> findAll(QueryRequest request, Long userId) {
        Page<StudentCourseVo> page = new Page<>(request.getPageNum(), request.getPageSize());
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;


        return this.baseMapper.findAll(page, week, userId);
    }

    @Override
    public CourseVo findById(Long courseId) {
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;

        return this.baseMapper.findById(week, courseId);
    }
}
