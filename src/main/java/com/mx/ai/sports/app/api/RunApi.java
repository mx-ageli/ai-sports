package com.mx.ai.sports.app.api;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.system.vo.ClassesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * 跑步相关接口
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
     * @param run 新增参数
     * @return
     */
    @ApiOperation(value = "#未实现 图5# 保存跑步数据")
    @ApiImplicitParam(name = "run", value = "新增参数", paramType = "body", dataType = "Run", required = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    AiSportsResponse<Boolean> add(@RequestBody @Valid Run run);

    /**
     * 查询合格的跑步规则
     *
     * @return
     */
    @ApiOperation(value = "#未实现 图6# 查询合格的跑步规则")
    @RequestMapping(value = "/find_run_rule", method = RequestMethod.GET)
    AiSportsResponse<List<RunRule>> findRunRule();

    /**
     * 按照指定的时间区间查询跑步记录
     *
     * @return
     */
    @ApiOperation(value = "#未实现 图10# 按照指定的时间区间查询跑步记录")
    @RequestMapping(value = "/find_run_history", method = RequestMethod.GET)
    AiSportsResponse<List<Object>> findRunHistory();

}
