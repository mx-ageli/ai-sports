package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.dto.ExportRecordStudentDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;

import java.util.Date;
import java.util.List;

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

    /**
     * 查询今天我报名的课程
     * @param week
     * @param userId
     * @return
     */
    List<CourseVo> findMyEntryByCurrent(int week, Long userId);

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<ExportRecordTotalDto> findExportRecordTotal(Date startTime, Date endTime);

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<ExportRecordStudentDto> findExportRecordStudent(Date startTime, Date endTime);
}