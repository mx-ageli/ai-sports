package com.mx.ai.sports.app.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.Course;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 课程相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "50-课程相关接口", protocols = "application/json")
@RequestMapping(value = "/api/course", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface CourseApi {

    /**
     * 查询我发布的课程列表（仅限于老师使用)
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#未实现 图12# 查询我发布的课程列表（仅限于老师使用)")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_my_publish", method = RequestMethod.POST)
    AiSportsResponse<IPage<Object>> findMyPublish(@RequestBody @Valid QueryRequest query);

    /**
     * 新增课程信息（仅限于老师使用)
     *
     * @param course 新增参数
     * @return
     */
    @ApiOperation(value = "#未实现 图30# 新增课程信息（仅限于老师使用)")
    @ApiImplicitParam(name = "course", value = "新增参数", paramType = "body", dataType = "Course", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid Course course);

    /**
     * 修改课程信息（仅限于老师使用)
     *
     * @param course 新增参数
     * @return
     */
    @ApiOperation(value = "#未实现 图30# 修改课程信息（仅限于老师使用)")
    @ApiImplicitParam(name = "course", value = "新增参数", paramType = "body", dataType = "Course", required = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update(@RequestBody @Valid Course course);

    /**
     * 查询课程的完成情况历史统计分析（仅限于老师使用)
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#未实现 图32# 查询课程的完成情况历史统计分析（仅限于老师使用)")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_history_analysis", method = RequestMethod.POST)
    AiSportsResponse<IPage<Object>> findHistoryAnalysis(@RequestBody @Valid QueryRequest query);

    /**
     * 查询学校所有的课程列表
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#未实现 图14# 查询学校所有的课程列表")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_all", method = RequestMethod.POST)
    AiSportsResponse<IPage<Object>> findAll(@RequestBody @Valid QueryRequest query);

    /**
     * 查询课程详细信息
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#未实现 图14# 查询课程详细信息")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    AiSportsResponse<Object> findById(@NotNull @RequestParam("courseId") Long courseId);

    /**
     * 学生报名课程（仅限于学生使用）
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#未实现 图15# 学生报名课程（仅限于学生使用）")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    AiSportsResponse<Boolean> entry(@NotNull @RequestParam("courseId") Long courseId);

    /**
     * 查询上课的历史记录
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#未实现 图16# 查询上课的历史记录")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_course_history", method = RequestMethod.POST)
    AiSportsResponse<IPage<Object>> findCourseHistory(@RequestBody @Valid QueryRequest query);
}
