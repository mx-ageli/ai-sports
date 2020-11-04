package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.entity.Keep;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.query.KeepAddVo;
import com.mx.ai.sports.course.query.KeepRecordQuery;
import com.mx.ai.sports.course.vo.CountVo;
import com.mx.ai.sports.course.vo.KeepRecordVo;

import java.util.List;
import java.util.Map;

/**
 * 健身记录
 *
 * @author Mengjiaxin
 * @date 2020/8/24 5:19 下午
 */
public interface IKeepService extends IService<Keep> {

    Boolean saveKeep(KeepAddVo keepAddVo, RunRule runRule, Long userId);

    IPage<KeepRecordVo> findKeepRecordVo(KeepRecordQuery query, Long userId);

    /**
     * 设置学生的运动是否合格
     * @param courseId
     * @param userId
     * @param isPass
     * @return
     */
    Boolean pass(Long courseId, Long userId, Boolean isPass);


    List<CountVo> findCountByCourseRecordId(Long courseRecordId);

    Keep findByCourseRecordUserId(Long courseRecordId, Long userId);

    Map<Long, Keep> findByCourseRecordIds(Long userId, List<Long> courseRecordIds);
}
