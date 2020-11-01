package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.RunApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.RunTypeEnum;
import com.mx.ai.sports.common.utils.DateUtil;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.KeepAddVo;
import com.mx.ai.sports.course.query.KeepRecordQuery;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.service.*;
import com.mx.ai.sports.course.vo.KeepRecordVo;
import com.mx.ai.sports.course.vo.RunRecordVo;
import com.mx.ai.sports.course.vo.RunRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    private IKeepService keepService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICourseStudentService courseStudentService;

    @Override
    @Log("保存跑步数据")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid RunAddVo runAddVo) {

        Course course = courseService.getById(runAddVo.getCourseId());
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }

        // 判断课程是否有跑步课程
        if (course.getIsRun() == null || !course.getIsRun()) {
            return new AiSportsResponse<Boolean>().fail().message("该课程不是跑步课程，不能保存跑步数据!");
        }

        // 开始时间小于结束时间
        if (runAddVo.getStartTime().after(runAddVo.getEndTime())) {
            return new AiSportsResponse<Boolean>().fail().message("结束时间不能小于开始时间！");
        }

        // 获取今天是星期几
        int week = DateUtil.getWeek();
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
        Long userId = getCurrentUserId();
        // 检查学生是否已经报课
        CourseStudent courseStudent = courseStudentService.findByUserCourseId(userId, course.getCourseId());
        if (courseStudent == null) {
            return new AiSportsResponse<Boolean>().fail().message("你没有报名这个课程，不能保存跑步数据！");
        }

        RunRule runRule = runRuleService.getById(RunTypeEnum.RUN.value());
        if (runRule == null) {
            return new AiSportsResponse<Boolean>().fail().message("没有设置跑步规则，请后台设置！");
        }
        // 如果跑步里程大于了设置的里程的5倍，不让保存
        if (runAddVo.getMileage() > runRule.getMileage() * 5) {
//            return new AiSportsResponse<Boolean>().fail().message("当前跑步里程与设置的里程范围不匹配！");
        }


        return new AiSportsResponse<Boolean>().success().data(runService.saveRun(runAddVo, runRule, userId));
    }

    @Override
    @Log("查询合格的跑步规则")
    public AiSportsResponse<RunRuleVo> findRunRule(@NotNull @RequestParam("runRuleId") Long runRuleId) {
        RunRule runRule = runRuleService.getById(runRuleId);
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

    @Override
    @Log("设置学生的运动是否合格")
    public AiSportsResponse<Boolean> pass(@NotNull @RequestParam("courseId") Long courseId, @NotNull @RequestParam("isPass") Boolean isPass) {
        Course course = courseService.getById(courseId);
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }
        // 判断课程是否有跑步课程
        if (course.getIsRun()) {
            return new AiSportsResponse<Boolean>().fail().message("该课程不是跑步课程，不能保存健身数据!");
        }
        // 获取今天是星期几
        int week = DateUtil.getWeek();
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 当前时间
        LocalTime currentTime = LocalTime.now();

        if (!course.getWeek().contains(String.valueOf(week))) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存运动数据！");
        }
        // 当前时间 < 开始时间
        if (currentTime.isBefore(startTime)) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存运动数据！");
        }
        Long userId = getCurrentUserId();
        // 检查学生是否已经报课
        CourseStudent courseStudent = courseStudentService.findByUserCourseId(userId, course.getCourseId());
        if (courseStudent == null) {
            return new AiSportsResponse<Boolean>().fail().message("你没有报名这个课程，不能保存健身数据！");
        }

        return new AiSportsResponse<Boolean>().success().data(keepService.pass(courseId, userId, isPass));
    }

    @Override
    public AiSportsResponse<Boolean> keepAdd(@RequestBody @Valid KeepAddVo keepAddVo) {
        Course course = courseService.getById(keepAddVo.getCourseId());
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }
        // 判断课程是否有跑步课程
        if (course.getIsRun()) {
            return new AiSportsResponse<Boolean>().fail().message("该课程不是健身课程，不能保存健身数据!");
        }

        // 开始时间小于结束时间
        if (keepAddVo.getStartTime().after(keepAddVo.getEndTime())) {
            return new AiSportsResponse<Boolean>().fail().message("结束时间不能小于开始时间！");
        }

        // 获取今天是星期几
        int week = DateUtil.getWeek();
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 当前时间
        LocalTime currentTime = LocalTime.now();

        if (!course.getWeek().contains(String.valueOf(week))) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存健身数据！");
        }
        // 当前时间 < 开始时间
        if (currentTime.isBefore(startTime)) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程还没有到上课时间，不能保存健身数据！");
        }

        RunRule runRule = runRuleService.getById(RunTypeEnum.KEEP.value());
        if (runRule == null) {
            return new AiSportsResponse<Boolean>().fail().message("没有设置健身规则，请后台设置！");
        }
        Long userId = getCurrentUserId();
        // 检查学生是否已经报课
        CourseStudent courseStudent = courseStudentService.findByUserCourseId(userId, course.getCourseId());
        if (courseStudent == null) {
            return new AiSportsResponse<Boolean>().fail().message("你没有报名这个课程，不能保存健身数据！");
        }

        return new AiSportsResponse<Boolean>().success().data(keepService.saveKeep(keepAddVo, runRule, userId));
    }

    @Override
    public AiSportsResponse<IPage<KeepRecordVo>> findKeepHistory(@RequestBody @Valid KeepRecordQuery query) {
        return new AiSportsResponse<IPage<KeepRecordVo>>().success().data(keepService.findKeepRecordVo(query, getCurrentUserId()));
    }

}
