package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.RunAddVo;

/**
 * 跑步记录
 * @author Mengjiaxin
 * @date 2020/8/24 5:19 下午
 */
public interface IRunService extends IService<Run> {

    /**
     *
     * @param runAddVo
     * @param runRule
     * @param userId
     * @return
     */
    Boolean saveRun(RunAddVo runAddVo, RunRule runRule, Long userId);

}
