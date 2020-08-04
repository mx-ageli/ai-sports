package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 班级相关接口
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "20-班级相关接口", protocols = "application/json")
@RequestMapping(value = "/api/classes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ClassesApi {

    /**
     * 创建一个班级（只能是老师才能创建，创建后默认为当前班级的管理员）
     * @return
     */
    @ApiOperation(value = "创建一个班级（只能是老师才能创建，创建后默认为当前班级的管理员） #未实现 #")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add();

    /**
     * 更改一个班级的信息（只能是创建班级的老师才可以修改）
     * @return
     */
    @ApiOperation(value = "更改一个班级的信息（只能是创建班级的老师才可以修改） #未实现 #")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    AiSportsResponse<Boolean> update();

    /**
     * 查询当前用户的班级列表
     * @return
     */
    @ApiOperation(value = "查询当前用户的班级列表 #未实现 #")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    AiSportsResponse<Boolean> list();





}