package com.mx.ai.sports.app.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.query.CourseQuery;
import com.mx.ai.sports.course.query.GroupAddVo;
import com.mx.ai.sports.course.query.GroupQuery;
import com.mx.ai.sports.course.query.GroupStudentUpdateVo;
import com.mx.ai.sports.course.query.GroupUpdateVo;
import com.mx.ai.sports.course.vo.GroupStudentVo;
import com.mx.ai.sports.course.vo.GroupVo;
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
 * 小组相关接口
 *
 * @author Mengjiaxin
 * @date 2020/9/24 2:05 下午
 */
@Validated
@Api(tags = "21-小组相关接口", protocols = "application/json")
@RequestMapping(value = "/api/group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface GroupApi {

    @ApiOperation(value = "#未实现# 查询一个课程下面的小组")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "CourseQuery", required = true)
    @RequestMapping(value = "/find_by_course_id", method = RequestMethod.POST)
    AiSportsResponse<IPage<GroupVo>> findByCourseId(@RequestBody @Valid CourseQuery query) throws AiSportsException;

    @ApiOperation(value = "#未实现# 查询一个老师对应的课程下面的所有小组")
    @ApiImplicitParam(name = "request", value = "查询参数", paramType = "body", dataType = "QueryRequest", required = true)
    @RequestMapping(value = "/find_by_user_id", method = RequestMethod.POST)
    AiSportsResponse<IPage<GroupVo>> findByUserId(@RequestBody @Valid QueryRequest request) throws AiSportsException;

    @ApiOperation(value = "#未实现# 查询一个小组内的学生列表")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "GroupQuery", required = true)
    @RequestMapping(value = "/find_student_by_course_id", method = RequestMethod.POST)
    AiSportsResponse<IPage<GroupStudentVo>> findStudentByCourseId(@RequestBody @Valid GroupQuery query) throws AiSportsException;

    @ApiOperation(value = "#未实现# 老师将学生换组，只能更改到相同课程的小组")
    @ApiImplicitParam(name = "updateVo", value = "小组学生更换参数", paramType = "body", dataType = "GroupStudentUpdateVo", required = true)
    @RequestMapping(value = "/update_student", method = RequestMethod.POST)
    AiSportsResponse<Boolean> updateStudent(@RequestBody @Valid GroupStudentUpdateVo updateVo) throws AiSportsException;

    @ApiOperation(value = "#未实现# 老师添加一个小组，只能给自己的课程添加小组")
    @ApiImplicitParam(name = "addVo", value = "新增小组参数", paramType = "body", dataType = "GroupAddVo", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid GroupAddVo addVo) throws AiSportsException;

    @ApiOperation(value = "#未实现# 修改组名名称，只能修改自己创建的小组")
    @ApiImplicitParam(name = "updateVo", value = "修改小组参数", paramType = "body", dataType = "GroupUpdateVo", required = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update(@RequestBody @Valid GroupUpdateVo updateVo) throws AiSportsException;

    @ApiOperation(value = "#未实现# 删除老师自己创建的空的小组")
    @ApiImplicitParam(name = "groupId", value = "小组Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    AiSportsResponse<Boolean> remove(@NotNull @RequestParam("groupId") Long groupId) throws AiSportsException;


}