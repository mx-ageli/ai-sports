package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.mapper.CourseStudentMapper;
import com.mx.ai.sports.course.service.ICourseStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseStudentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements ICourseStudentService {


    @Override
    public List<Long> findByCourseId(Long courseId) {
        List<CourseStudent> courseStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId));
        if(CollectionUtils.isEmpty(courseStudentList)){
            return new ArrayList<>();
        }
        return courseStudentList.stream().map(CourseStudent::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findByUserId(Long userId) {
        List<CourseStudent> courseStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getUserId, userId));
        if(CollectionUtils.isEmpty(courseStudentList)){
            return new ArrayList<>();
        }
        return courseStudentList.stream().map(CourseStudent::getUserId).collect(Collectors.toList());
    }

    @Override
    public CourseStudent findByUserCourseId(Long userId, Long courseId) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId));
    }

    @Override
    public Boolean remove(Long userId, Long courseId) {
        return this.baseMapper.delete(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId)) > 0;
    }
}
