package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.RunStatusEnum;
import com.mx.ai.sports.course.entity.Keep;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.mapper.KeepMapper;
import com.mx.ai.sports.course.query.KeepAddVo;
import com.mx.ai.sports.course.query.KeepRecordQuery;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.IKeepService;
import com.mx.ai.sports.course.service.IRunService;
import com.mx.ai.sports.course.vo.KeepRecordVo;
import com.mx.ai.sports.course.vo.RunRecordDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("KeepService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KeepServiceImpl extends ServiceImpl<KeepMapper, Keep> implements IKeepService {

    @Autowired
    private ICourseRecordService courseRecordService;

    @Autowired
    private IRunService runService;

    @Override
    public Boolean saveKeep(KeepAddVo keepAddVo, RunRule runRule, Long userId) {
        Keep keep = new Keep();
        keep.setCourseId(keepAddVo.getCourseId());
        keep.setUserId(userId);
        keep.setCreateTime(new Date());
        keep.setStartTime(keepAddVo.getStartTime());
        keep.setEndTime(keepAddVo.getEndTime());
        keep.setKeepTime(keepAddVo.getKeepTime());
        keep.setStatus(RunStatusEnum.NO_PASS.value());
        // 对应最新的课程记录Id
        Long courseRecordId = courseRecordService.findIdByNowAndCreate(keepAddVo.getCourseId());
        keep.setCourseRecordId(courseRecordId);
        // 先清除历史数据， 只保存最后一次运动记录
        deleteHistory(courseRecordId, userId);

        // 查询当前这次健身是否满足规则，满足就合格
        if (keepAddVo.getKeepTime() >= runRule.getRunTime()) {
            keep.setStatus(RunStatusEnum.PASS.value());
        }
        boolean success = this.save(keep);

        // 重新计算所有学生的合格状态
        boolean isPass = Objects.equals(keep.getStatus(), RunStatusEnum.PASS.value());
        runService.calcPass(userId, isPass, keepAddVo.getCourseId(), courseRecordId);

        return success;
    }

    /**
     * 先删除以往的记录
     *
     * @param courseRecordId
     * @param userId
     */
    private void deleteHistory(Long courseRecordId, Long userId) {
        this.baseMapper.delete(new LambdaQueryWrapper<Keep>().eq(Keep::getCourseRecordId, courseRecordId).eq(Keep::getUserId, userId));
    }

    @Override
    public IPage<KeepRecordVo> findKeepRecordVo(KeepRecordQuery query, Long userId) {
        Page<RunRecordDetailVo> page = new Page<>(query.getRequest().getPageNum(), query.getRequest().getPageSize());
        return baseMapper.findKeepRecordVo(page, userId, query.getStatus());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean pass(Long courseId, Long userId, Boolean isPass) {
        Keep keep = new Keep();
        keep.setCourseId(courseId);
        keep.setUserId(userId);
        keep.setCreateTime(new Date());

        keep.setStatus(RunStatusEnum.NO_PASS.value());
        // 对应最新的课程记录Id
        Long courseRecordId = courseRecordService.findIdByNowAndCreate(courseId);
        keep.setCourseRecordId(courseRecordId);
        // 先清除历史数据， 只保存最后一次运动记录
        deleteHistory(courseRecordId, userId);

        // 合格设置为pass
        if (isPass) {
            keep.setStatus(RunStatusEnum.PASS.value());
        }
        boolean success = this.save(keep);
        runService.calcPass(userId, isPass, courseId, courseRecordId);

        return success;
    }
}