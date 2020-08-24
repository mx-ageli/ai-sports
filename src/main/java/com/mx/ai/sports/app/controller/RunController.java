package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mx.ai.sports.app.api.RunApi;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.RunStatusEnum;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.service.IRunRuleService;
import com.mx.ai.sports.course.service.IRunService;
import com.mx.ai.sports.course.vo.RunRecordVo;
import com.mx.ai.sports.course.vo.RunRuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


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

    @Override
    public AiSportsResponse<Boolean> add(@RequestBody @Valid RunAddVo runAddVo) {
        // 开始时间小于结束时间
        if (runAddVo.getStartTime().after(runAddVo.getEndTime())) {
            return new AiSportsResponse<Boolean>().fail().message("结束时间不能小于开始时间！");
        }

        Run run = new Run();
        run.setCourseId(runAddVo.getCourseId());
        run.setUserId(getCurrentUserId());
        run.setCreateTime(new Date());
        run.setStartTime(runAddVo.getStartTime());
        run.setEndTime(runAddVo.getEndTime());
        run.setLocation(runAddVo.getLocation());
        run.setLocationName(runAddVo.getLocationName());
        run.setRunTime(runAddVo.getRunTime());
        run.setMileage(runAddVo.getMileage());
        run.setSpeed(runAddVo.getSpeed());
        run.setStatus(RunStatusEnum.NO_PASS.value());

        RunRule runRule = runRuleService.getOne(new LambdaQueryWrapper<>());
        if (runRule == null) {
            return new AiSportsResponse<Boolean>().fail().message("没有设置跑步规则，请后台设置！");
        }
        // 查询当前这次跑步是否满足规则，满足就合格
        if(runRule.getMileage() > runAddVo.getMileage() && runRule.getRunTime() > runAddVo.getRunTime()){
            run.setStatus(RunStatusEnum.PASS.value());
        }

        return new AiSportsResponse<Boolean>().success().data(runService.save(run));
    }

    @Override
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
    public AiSportsResponse<List<RunRecordVo>> findRunHistory(@RequestBody @Valid RunRecordQuery query) {
        return null;
    }

}
