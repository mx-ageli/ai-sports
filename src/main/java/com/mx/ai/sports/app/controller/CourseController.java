package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.CourseApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.query.CourseQuery;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.vo.CourseNumVo;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest query) {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> add(@RequestBody @Valid CourseUpdateVo course) {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> update(@RequestBody @Valid CourseUpdateVo course) {
        return null;
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
