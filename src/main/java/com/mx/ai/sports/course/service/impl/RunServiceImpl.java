package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.RunStatusEnum;
import com.mx.ai.sports.course.entity.*;
import com.mx.ai.sports.course.mapper.RunMapper;
import com.mx.ai.sports.course.query.RunAddVo;
import com.mx.ai.sports.course.query.RunLocationAddVo;
import com.mx.ai.sports.course.query.RunRecordQuery;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.course.service.IRunLocationService;
import com.mx.ai.sports.course.service.IRunService;
import com.mx.ai.sports.course.vo.RunRecordDetailVo;
import com.mx.ai.sports.course.vo.RunRecordVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private ICourseRecordService courseRecordService;

    @Autowired
    private IRecordStudentService recordStudentService;

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
        run.setRuleRunTime(runRule.getRunTime());
        run.setRuleMileage(runRule.getMileage());
        run.setSpeed(runAddVo.getSpeed());
        run.setStatus(RunStatusEnum.NO_PASS.value());
        // 对应最新的课程记录Id
        Long courseRecordId = courseRecordService.findIdByNowAndCreate(runAddVo.getCourseId());
        run.setCourseRecordId(courseRecordId);
        // 先清除历史数据， 只保存最后一次运动记录
        deleteHistory(courseRecordId, userId);

        // 查询当前这次跑步是否满足规则，满足就合格
        if (runAddVo.getMileage() > runRule.getMileage() && runAddVo.getRunTime() > runRule.getRunTime()) {
            run.setStatus(RunStatusEnum.PASS.value());
        }
        this.save(run);

        // 重新计算学生的合格状态
        calcPass(userId, run, runAddVo.getCourseId(), courseRecordId);

        List<RunLocation> runList = new ArrayList<>();
        for (RunLocationAddVo addVo : runAddVo.getLocation()) {
            RunLocation runLocation = new RunLocation();
            runLocation.setLat(addVo.getLat());
            runLocation.setLon(addVo.getLon());
            runLocation.setRunId(run.getRunId());
            runLocation.setTime(addVo.getTime());
            runList.add(runLocation);
        }

        return runLocationService.saveBatch(runList);
    }

    /**
     * 先删除以往的记录
     * @param courseRecordId
     * @param userId
     */
    private void deleteHistory(Long courseRecordId, Long userId){
        this.baseMapper.delete(new LambdaQueryWrapper<Run>().eq(Run::getCourseRecordId, courseRecordId).eq(Run::getUserId, userId));
    }

    @Override
    public RunRecordVo getRunRecordVo(Long currentUserId, RunRecordQuery query) {

        RunRecordVo runRecordVo = baseMapper.getRunRecordCountVo(currentUserId, query.getStartTime(), query.getEndTime());

        Page<RunRecordDetailVo> page = new Page<>(query.getRequest().getPageNum(), query.getRequest().getPageSize());

        IPage<RunRecordDetailVo> detailPage = baseMapper.findRunRecordDetailVo(page, currentUserId, query.getStartTime(), query.getEndTime(), query.getStatus());

        runRecordVo.setDetailPage(detailPage);

        return runRecordVo;
    }

    @Override
    public Map<Long, Run> findByCourseRecordIds(Long userId, List<Long> courseRecordIds) {
        List<Run> runList = baseMapper.selectList(new LambdaQueryWrapper<Run>().eq(Run::getUserId, userId).in(Run::getCourseRecordId, courseRecordIds));
        if (CollectionUtils.isEmpty(runList)) {
            return new HashMap<>(0);
        }
        return runList.stream().collect(Collectors.toMap(Run::getCourseRecordId, Function.identity(), (e1, e2) -> e1));
    }

    /**
     * 重新计算学生的合格状态
     *
     * @param userId
     * @param run
     * @param courseRecordId
     */
    private void calcPass(Long userId, Run run, Long courseId, Long courseRecordId) {
        // 需要将是否合格保存到学生记录表中
        RecordStudent recordStudent = recordStudentService.getOne(new LambdaQueryWrapper<RecordStudent>().eq(RecordStudent::getUserId, userId).eq(RecordStudent::getCourseRecordId, courseRecordId));
        // 没有学生记录 需要创建
        if(recordStudent == null){
            recordStudent = new RecordStudent(courseId, courseRecordId, userId);
        }
        recordStudent.setIsPass(Objects.equals(run.getStatus(), RunStatusEnum.PASS.value()));
        recordStudentService.saveOrUpdate(recordStudent);

        // 还需要重新统计课程记录的合格人数
        CourseRecord courseRecord = courseRecordService.getById(courseRecordId);
        if (recordStudent.getIsPass()) {
            // 合格人数+1
            courseRecord.setPassCount(courseRecord.getPassCount() + 1);
        } else {
            // 不合格人数+1
            courseRecord.setNoPassCount(courseRecord.getNoPassCount() + 1);
        }
        courseRecordService.saveOrUpdate(courseRecord);
    }
}
