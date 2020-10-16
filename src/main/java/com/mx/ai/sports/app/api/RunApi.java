package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.vo.RunRecordVo;
import com.mx.ai.sports.course.vo.RunRuleVo;
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
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 跑步相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:24 下午
 */
@Validated
@Api(tags = "60-跑步相关接口", protocols = "application/json")
@RequestMapping(value = "/api/run", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RunApi {

    /**
     * 保存跑步数据
     *
     * @param runAddVo 新增参数
     * @return
     */
    @ApiOperation(value = "#已实现 图5# 保存跑步数据")
    @ApiImplicitParam(name = "runAddVo", value = "新增参数", paramType = "body", dataType = "RunAddVo", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid RunAddVo runAddVo);

    /**
     * 查询合格的跑步规则
     *
     * @return
     */
    @ApiOperation(value = "#已实现 图6# 查询合格的跑步规则")
    @RequestMapping(value = "/find_run_rule", method = RequestMethod.GET)
    AiSportsResponse<RunRuleVo> findRunRule();

    /**
     * 按照指定的时间区间查询跑步记录
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "#已实现 图10# 按照指定的时间区间查询跑步记录")
    @ApiImplicitParam(name = "query", value = "查询参数", paramType = "body", dataType = "RunRecordQuery", required = true)
    @RequestMapping(value = "/find_run_history", method = RequestMethod.POST)
    AiSportsResponse<RunRecordVo> findRunHistory(@RequestBody @Valid RunRecordQuery query);

    /**
     * 设置学生的运动是否合格
     *
     * @param courseId
     * @param isPass
     * @return
     */
    @ApiOperation(value = "#已实现 # 设置学生的运动是否合格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程Id", paramType = "query", dataType = "long", required = true),
            @ApiImplicitParam(name = "isPass", value = "是否合格", paramType = "query", dataType = "boolean", required = true),
    })
    @RequestMapping(value = "/pass", method = RequestMethod.GET)
    AiSportsResponse<Boolean> pass(@NotNull @RequestParam("courseId") Long courseId, @NotNull @RequestParam("isPass") Boolean isPass);

}
