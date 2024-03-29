package com.mx.ai.sports.app.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.query.*;
import com.mx.ai.sports.course.vo.*;
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
import java.util.List;

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
     * @param request 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图12# 查询我发布的课程列表（仅限于老师使用)")
    @ApiImplicitParam(name = "request", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_my_publish", method = RequestMethod.POST)
    AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest request) throws AiSportsException;

    /**
     * 新增课程信息（仅限于老师使用)
     *
     * @param addVo
     * @return
     * @throws AiSportsException
     */
    @ApiOperation(value = "#已实现 图30# 新增课程信息（仅限于老师使用)",
            notes = "1、星期： 分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 \n" +
                    "2、小时格式时间： HH:mm 只需要小时和分钟 \n" +
                    "3、时间范围：开始时间必须在结束时间之前， 可打卡时间必须在开始时间之前\n" +
                    "4、当前课程正在进行中，不能修改! 请等本次课程结束后再修改！")
    @ApiImplicitParam(name = "addVo", value = "新增参数", paramType = "body", dataType = "CourseAddVo", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid CourseAddVo addVo) throws AiSportsException;

    /**
     * 修改课程信息（仅限于老师使用)
     *
     * @param updateVo
     * @return
     * @throws AiSportsException
     */
    @ApiOperation(value = "#已实现 图30# 修改课程信息（仅限于老师使用)")
    @ApiImplicitParam(name = "updateVo", value = "新增参数", paramType = "body", dataType = "CourseUpdateVo", required = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update(@RequestBody @Valid CourseUpdateVo updateVo) throws AiSportsException;

    /**
     * 查询某一个课程报名数量、缺席数量、合格数量、不合格数量
     *
     * @param courseRecordId 课程记录Id
     * @return
     */
    @ApiOperation(value = "#已实现 图44# 查询某一个课程报名数量、缺席数量、合格数量、不合格数量")
    @ApiImplicitParam(name = "courseRecordId", value = "课程记录Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/find_num_by_id", method = RequestMethod.GET)
    AiSportsResponse<CourseNumVo> findNumById(@NotNull @RequestParam("courseRecordId") Long courseRecordId);

    /**
     * 查询某一个课程的完成情的学生列表（仅限于老师使用)
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图44# 查询某一个课程的完成情的学生列表（仅限于老师使用)")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "StudentCourseQuery", required = true)
    @RequestMapping(value = "/find_student_by_id", method = RequestMethod.POST)
    AiSportsResponse<IPage<StudentCourseVo>> findStudentById(@RequestBody @Valid StudentCourseQuery query) throws AiSportsException;


    /**
     * 查询所有的课程列表
     *
     * @param request 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图14# 查询所有的课程列表-今日课程会排在最前面")
    @ApiImplicitParam(name = "request", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_all", method = RequestMethod.POST)
    AiSportsResponse<IPage<CourseVo>> findAll(@RequestBody @Valid QueryRequest request);

    /**
     * 查询所有的课程列表
     *
     * @param request 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图14# 查询我已经报名的课程-今日课程会排在最前面")
    @ApiImplicitParam(name = "request", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_my_entry", method = RequestMethod.POST)
    AiSportsResponse<IPage<CourseVo>> findMyEntry(@RequestBody @Valid QueryRequest request);

    /**
     * 查询课程详细信息
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#已实现 图14# 查询课程详细信息")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/find_by_id", method = RequestMethod.GET)
    AiSportsResponse<CourseVo> findById(@NotNull @RequestParam("courseId") Long courseId);

    /**
     * 学生报名课程，或者是取消报名（仅限于学生使用）
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#已实现 图15# 学生报名课程（仅限于学生使用）",
            notes = "1、在课程的开始时间到结束时间的范围内，不能报名或取消报名")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    AiSportsResponse<CourseEntryVo> entry(@NotNull @RequestParam("courseId") Long courseId) throws AiSportsException;

    /**
     * 学生报名课程，或者是取消报名（仅限于学生使用）
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#已实现 图15# 学生取消报名（仅限于学生使用）",
            notes = "1、在课程的开始时间到结束时间的范围内，不能报名或取消报名")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/cancel_entry", method = RequestMethod.GET)
    AiSportsResponse<CourseEntryVo> cancelEntry(@NotNull @RequestParam("courseId") Long courseId) throws AiSportsException;

    /**
     * 查询课程的完成情况历史统计分析（仅限于老师使用)
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图32# 查询课程的完成情况历史统计分析（仅限于老师使用)")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "CourseQuery", required = true)
    @RequestMapping(value = "/find_history_analysis", method = RequestMethod.POST)
    AiSportsResponse<IPage<CourseRecordVo>> findHistoryAnalysis(@RequestBody @Valid CourseQuery query) throws AiSportsException;

    /**
     * 查询上课的历史记录
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图16# 查询上课的历史记录")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "UserCourseQuery", required = true)
    @RequestMapping(value = "/find_course_history", method = RequestMethod.POST)
    AiSportsResponse<IPage<RecordStudentVo>> findCourseHistory(@RequestBody @Valid UserCourseQuery query);

    /**
     * 查询我报名的正在进行的课程
     *
     * @return
     */
    @ApiOperation(value = "#已实现 # 查询我报名的正在进行的课程")
    @RequestMapping(value = "/find_my_entry_by_current", method = RequestMethod.GET)
    AiSportsResponse<List<CourseVo>> findMyEntryByCurrent();
}
