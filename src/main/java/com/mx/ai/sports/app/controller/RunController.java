package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mx.ai.sports.app.api.RunApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.IRunRuleService;
import com.mx.ai.sports.course.service.IRunService;
import com.mx.ai.sports.course.vo.RunRecordVo;
import com.mx.ai.sports.course.vo.RunRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * 跑步相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("RunApi")
public class RunController extends BaseRestController implements RunApi {

    @Autowired
    private IRunRuleService runRuleService;

    @Autowired
    private IRunService runService;

    @Autowired
    private ICourseService courseService;

    @Override
    @Log("保存跑步数据")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid RunAddVo runAddVo) {

        Course course = courseService.getById(runAddVo.getCourseId());
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }
        // 开始时间小于结束时间
        if (runAddVo.getStartTime().after(runAddVo.getEndTime())) {
            return new AiSportsResponse<Boolean>().fail().message("结束时间不能小于开始时间！");
        }

        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 当前时间
        LocalTime currentTime = LocalTime.now();

        if (!course.getWeek().contains(String.valueOf(week))) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存跑步数据！");
        }
        // 当前时间 < 开始时间
        if (currentTime.isBefore(startTime)) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存跑步数据！");
        }

        RunRule runRule = runRuleService.getOne(new LambdaQueryWrapper<>());
        if (runRule == null) {
            return new AiSportsResponse<Boolean>().fail().message("没有设置跑步规则，请后台设置！");
        }

        return new AiSportsResponse<Boolean>().success().data(runService.saveRun(runAddVo, runRule, getCurrentUserId()));
    }

    @Override
    @Log("查询合格的跑步规则")
    public AiSportsResponse<RunRuleVo> findRunRule() {
        RunRule runRule = runRuleService.getOne(new LambdaQueryWrapper<>());
        if (runRule == null) {
            return new AiSportsResponse<RunRuleVo>().fail().message("没有设置跑步规则，请后台设置！");
        }
        RunRuleVo vo = new RunRuleVo();
        vo.setMileage(runRule.getMileage());
        vo.setRunTime(runRule.getRunTime());

        return new AiSportsResponse<RunRuleVo>().success().data(vo);
    }

    @Override
    @Log("按照指定的时间区间查询跑步记录")
    public AiSportsResponse<RunRecordVo> findRunHistory(@RequestBody @Valid RunRecordQuery query) {

        return new AiSportsResponse<RunRecordVo>().success().data(runService.getRunRecordVo(getCurrentUserId(), query));
    }

}
