package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.dto.ExportRecordStudentDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.query.CourseAddVo;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.vo.CourseVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ICourseService extends IService<Course> {

    /**
     * 通过课程名称查找课程, 如果courseId不为空就排除
     *
     * @param courseId   排除的课程Id
     * @param courseName 课程名称
     * @return
     */
    Course findByCourseName(Long courseId, String courseName);

    /**
     * 查询所有的课程
     *
     * @param request
     * @param userId
     * @return
     */
    IPage<CourseVo> findAll(QueryRequest request, Long userId);

    /**
     * 查询课程详情
     *
     * @param courseId
     * @return
     */
    CourseVo findById(Long courseId);

    /**
     * 查询学生报名的课程
     *
     * @param request
     * @param currentUserId
     * @return
     */
    IPage<CourseVo> findMyEntry(QueryRequest request, Long currentUserId);

    /**
     * 老师新增课程，并创建定时任务
     *
     * @param addVo
     * @return
     */
    Boolean saveCourse(CourseAddVo addVo, Long currentUserId) throws AiSportsException;

    /**
     * 老师修改课程，并创建定时任务
     *
     * @param course
     * @return
     */
    Boolean updateCourse(Course course, Long currentUserId);


    /**
     * 查询我报名的正在进行的课程
     *
     * @param
     * @return
     */
    List<CourseVo> findMyEntryByCurrent(Long currentUserId);

    /**
     * 查询时间范围内的课程统计数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<ExportRecordTotalDto> findExportRecordTotal(Date startTime, Date endTime);

    /**
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Map<String, List<ExportRecordStudentDto>> findExportRecordStudent(Date startTime, Date endTime);
}
