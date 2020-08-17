package com.mx.ai.sports.app.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.query.ClassesUpdateVo;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.vo.UserSmallVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 班级相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "20-班级相关接口", protocols = "application/json")
@RequestMapping(value = "/api/classes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ClassesApi {

    /**
     * 创建一个班级（只能是老师才能创建，创建后默认为当前班级的管理员）
     *
     * @param classes 新增参数
     * @return
     */
    @ApiOperation(value = "#已实现 图27# 创建一个班级（只能是老师才能创建，创建后默认为当前班级的管理员）")
    @ApiImplicitParam(name = "classes", value = "新增参数", paramType = "body", dataType = "ClassesUpdateVo", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid ClassesUpdateVo classes) throws AiSportsException;

    /**
     * 更改一个班级的信息（只能是创建班级的老师才可以修改）
     *
     * @param classes 新增参数
     * @return
     */
    @ApiOperation(value = "#已实现 图25# 更改一个班级的信息（只能是创建班级的老师才可以修改）")
    @ApiImplicitParam(name = "classes", value = "修改参数", paramType = "body", dataType = "ClassesUpdateVo", required = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update(@RequestBody @Valid ClassesUpdateVo classes) throws AiSportsException;

    /**
     * 查询当前用户的班级列表
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-08-05# 查询当前用户的班级列表")
    @RequestMapping(value = "/find_all", method = RequestMethod.GET)
    AiSportsResponse<List<ClassesVo>> findAll() throws AiSportsException;

    /**
     * 查询一个班级中的学生列表
     *
     * @param query 查询参数
     * @return
     */
    @ApiOperation(value = "#已实现 图24# 查询一个班级中的学生列表")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "ClassesQuery", required = true)
    @RequestMapping(value = "/find_student_by_classes_id", method = RequestMethod.POST)
    AiSportsResponse<IPage<UserSmallVo>> findStudentByClassesId(@RequestBody @Valid ClassesQuery query);

}