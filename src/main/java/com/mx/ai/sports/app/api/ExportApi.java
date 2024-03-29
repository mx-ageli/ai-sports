package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.query.ExportAllQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 导入导出相关接口
 * @author Mengjiaxin
 * @date 2020/9/18 9:50 上午
 */
@Validated
@Api(tags = "91-导入导出相关接口", protocols = "application/json")
@RequestMapping(value = "/api/export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ExportApi {

    /**
     * 导出时间段内的课程统计数据
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-09-18# 导出时间段内的课程统计数据")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "ExportAllQuery", required = true)
    @RequestMapping(value = "/export_all", method = RequestMethod.POST)
    AiSportsResponse<String> exportAll(@RequestBody @Valid ExportAllQuery query);


    /**
     * 导入学校老师-初始化老师信息
     *
     * @param file 上传的文件
     * @return
     */
    @ApiOperation(value = "#已实现 2020-10-14# 导入学校老师-初始化老师信息")
    @RequestMapping(value = "/import_teacher", method = RequestMethod.POST)
    AiSportsResponse<Boolean> importTeacher(@NotNull MultipartFile file);


    /**
     * 导入学生基础信息-初始化主课、班级、学生信息
     *
     * @param file 上传的文件
     * @return
     */
    @ApiOperation(value = "#已实现 2020-10-14# 导入学生基础信息-初始化主课、班级、学生信息")
    @RequestMapping(value = "/import_student", method = RequestMethod.POST)
    AiSportsResponse<Boolean> importStudent(@NotNull MultipartFile file);

    /**
     * 导出时间段内的课程统计数据
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-12-13# 导出某一个学期内的学生合格上课记录")
    @ApiImplicitParam(name = "termId", value = "学期Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/export_student_record", method = RequestMethod.POST)
    AiSportsResponse<String> exportStudentRecord(@NotNull @RequestParam("termId") Long termId);

    /**
     * 导出某一个学期内的学生平时成绩表
     *
     * @return
     */
    @ApiOperation(value = "#已实现 2020-12-30# 导出某一个学期内的学生平时成绩表")
    @ApiImplicitParam(name = "termId", value = "学期Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/export_student_score", method = RequestMethod.POST)
    AiSportsResponse<String> exportStudentScore(@NotNull @RequestParam("termId") Long termId);

}