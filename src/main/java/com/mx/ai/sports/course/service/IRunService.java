package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.vo.RunRecordVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询学生的跑步记录
     * @param currentUserId
     * @param query
     * @return
     */
    RunRecordVo getRunRecordVo(Long currentUserId, RunRecordQuery query);

    Map<Long, Run> findByCourseRecordIds(Long userId, List<Long> courseRecordIds);

    /**
     * 查询时间内课程累计的跑步数量
     * @param startTime
     * @param endTime
     * @return
     */
    Map<Long, Long> findCourseRunCount(Date startTime, Date endTime);


    void calcPass(Long userId, boolean isPass, Long courseId, Long courseRecordId);
}
