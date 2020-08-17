package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.CourseApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.converter.CourseConverter;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.query.CourseQuery;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.vo.CourseNumVo;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import com.mx.ai.sports.job.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 课程相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("CourseApi")
public class CourseController extends BaseRestController implements CourseApi {

    @Autowired
    private CourseConverter courseConverter;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IJobService jobService;

    @Override
    public AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest query) {
        return null;
    }

    @Override
    @TeacherRole
    @Log("老师创建课程")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid CourseUpdateVo updateVo) {

        // TODO 判断限制只能是老师才能调用这个接口

        // TODO 判断课程名称是否重复

        // TODO 判断发布的课程时间是否与我之前发布的课程时间冲突， 先判断星期，判断时间

        Course course = courseConverter.vo2Domain(updateVo);
        course.setUserId(getCurrentUserId());

//        courseService.save(course);

        // 创建课程的定时任务
        // jobService.createJob();
        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    @Log("老师修改课程")
    public AiSportsResponse<Boolean> update(@RequestBody @Valid CourseUpdateVo updateVo) {
        // TODO 判断课程名称是否重复

        // TODO 判断发布的课程时间是否与我之前发布的课程时间冲突， 先判断星期，判断时间

        Course course = courseConverter.vo2Domain(updateVo);
        course.setUserId(getCurrentUserId());

        courseService.saveOrUpdate(course);
        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    public AiSportsResponse<IPage<Object>> findHistoryAnalysis(@RequestBody @Valid CourseQuery query) {
        return null;
    }

    @Override
    public AiSportsResponse<CourseNumVo> findNumById(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<StudentCourseVo>> findStudentById(@RequestBody @Valid StudentCourseQuery query) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<CourseVo>> findAll(@RequestBody @Valid QueryRequest query) {
        return null;
    }

    @Override
    public AiSportsResponse<CourseVo> findById(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> entry(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<Object>> findCourseHistory(@RequestBody @Valid QueryRequest query) {
        return null;
    }
}
