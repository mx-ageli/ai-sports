package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.RunStatusEnum;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunLocation;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.mapper.RunMapper;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunLocationAddVo;
import com.mx.ai.sports.course.service.IRunLocationService;
import com.mx.ai.sports.course.service.IRunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("RunService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RunServiceImpl extends ServiceImpl<RunMapper, Run> implements IRunService {

    @Autowired
    private IRunLocationService runLocationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRun(RunAddVo runAddVo, RunRule runRule, Long userId) {

        Run run = new Run();
        run.setCourseId(runAddVo.getCourseId());
        run.setUserId(userId);
        run.setCreateTime(new Date());
        run.setStartTime(runAddVo.getStartTime());
        run.setEndTime(runAddVo.getEndTime());
        run.setRunTime(runAddVo.getRunTime());
        run.setMileage(runAddVo.getMileage());
        run.setSpeed(runAddVo.getSpeed());
        run.setStatus(RunStatusEnum.NO_PASS.value());

        // 查询当前这次跑步是否满足规则，满足就合格
        if(runRule.getMileage() > runAddVo.getMileage() && runRule.getRunTime() > runAddVo.getRunTime()){
            run.setStatus(RunStatusEnum.PASS.value());
        }
        this.save(run);

        List<RunLocation> runList = new ArrayList<>();
        for(RunLocationAddVo addVo : runAddVo.getLocation()){
            RunLocation runLocation = new RunLocation();
            runLocation.setLat(addVo.getLat());
            runLocation.setLon(addVo.getLon());
            runLocation.setRunId(run.getRunId());
            runLocation.setTime(addVo.getTime());
            runList.add(runLocation);
        }

        return runLocationService.saveBatch(runList);
    }
}
