package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.query.SignedAddVo;
import com.mx.ai.sports.course.vo.SignedVo;
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
 * 打卡签到相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 3:53 下午
 */
@Validated
@Api(tags = "40-打卡签到相关接口", protocols = "application/json")
@RequestMapping(value = "/api/signed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SignedApi {


    /**
     * 通过课程Id查询我最近的一次打卡记录
     *
     * @param courseId 课程Id
     * @return
     */
    @ApiOperation(value = "#已实现 图6# 通过课程Id查询我最近的一次打卡记录")
    @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/find_last_by_course_id", method = RequestMethod.GET)
    AiSportsResponse<SignedVo> findLastByCourseId(@NotNull @RequestParam("courseId") Long courseId);

    /**
     * 通过课程记录Id查询我以往的课程打卡记录
     *
     * @param courseRecordId 课程记录Id
     * @return
     */
    @ApiOperation(value = "#已实现 图6# 通过课程记录Id查询我以往的课程打卡记录")
    @ApiImplicitParam(name = "courseRecordId", value = "课程记录Id", paramType = "query", dataType = "long", required = true)
    @RequestMapping(value = "/find_by_course_record_id", method = RequestMethod.GET)
    AiSportsResponse<SignedVo> findByCourseRecordId(@NotNull @RequestParam("courseRecordId") Long courseRecordId);

    /**
     * 打卡
     *
     * @param signedAddVo 打卡参数
     * @return
     */
    @ApiOperation(value = "#已实现 图6# 打卡")
    @ApiImplicitParam(name = "signedAddVo", value = "打卡参数", paramType = "body", dataType = "SignedAddVo", required = true)
    @RequestMapping(value = "/signed", method = RequestMethod.POST)
    AiSportsResponse<Boolean> signed(@RequestBody @Valid SignedAddVo signedAddVo);

}
