package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.entity.SignedStatusEnum;
import com.mx.ai.sports.course.entity.Keep;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.mapper.RecordStudentMapper;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.service.IKeepService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.course.service.IRunService;
import com.mx.ai.sports.course.vo.RecordStudentVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("RecordStudentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordStudentServiceImpl extends ServiceImpl<RecordStudentMapper, RecordStudent> implements IRecordStudentService {

    @Autowired
    private IRunService runService;

    @Autowired
    private IKeepService keepService;

    @Override
    public List<Long> findUserIdByCourseRecordId(Long courseRecordId) {
        List<RecordStudent> recordStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<RecordStudent>().eq(RecordStudent::getCourseRecordId, courseRecordId));
        if (CollectionUtils.isEmpty(recordStudentList)) {
            return new ArrayList<>();
        }
        return recordStudentList.stream().map(RecordStudent::getUserId).collect(Collectors.toList());
    }

    @Override
    public IPage<StudentCourseVo> findVoByCourseRecordId(StudentCourseQuery query) {
        Page<StudentCourseVo> page = new Page<>(query.getRequest().getPageNum(), query.getRequest().getPageSize());

        return this.baseMapper.findVoByCourseRecordId(page, query.getCourseRecordId(), query.getType());
    }

    @Override
    public IPage<RecordStudentVo> findRecordStudentVo(QueryRequest request, Long userId) {
        Page<RecordStudentVo> page = new Page<>(request.getPageNum(), request.getPageSize());

        IPage<RecordStudentVo> recordPage = this.baseMapper.findRecordStudentVo(page, userId);

        if (!CollectionUtils.isEmpty(recordPage.getRecords())) {
            List<Long> courseRecordIds = recordPage.getRecords().stream().map(RecordStudentVo::getCourseRecordId).collect(Collectors.toList());
            Map<Long, Run> runMap = runService.findByCourseRecordIds(userId, courseRecordIds);

            Map<Long, Keep> keepMap = keepService.findByCourseRecordIds(userId, courseRecordIds);

            for (RecordStudentVo vo : recordPage.getRecords()){
                if(runMap.containsKey(vo.getCourseRecordId())){
                    Run run = runMap.get(vo.getCourseRecordId());
                    vo.setMileage(run.getMileage());
                    vo.setRunTime(run.getRunTime());
                    vo.setRuleMileage(run.getRuleMileage());
                    vo.setRuleRunTime(run.getRuleRunTime());
                } else if(keepMap.containsKey(vo.getCourseRecordId())){
                    Keep keep = keepMap.get(vo.getCourseRecordId());
                    vo.setRunTime(keep.getKeepTime());
                }

                vo.setSignedStatus(SignedStatusEnum.NORMAL.value());
                if (vo.getEndTime() == null) {
                    vo.setSignedStatus(SignedStatusEnum.NO_SIGNED.value());
                }
                if (vo.getIsAbsent()) {
                    vo.setSignedStatus(SignedStatusEnum.ABSENT.value());
                }
                if (vo.getIsLate()) {
                    vo.setSignedStatus(SignedStatusEnum.LATE.value());
                }

            }
        }

        return recordPage;
    }

    @Override
    public RecordStudent findByCourseRecordIdAndUserId(Long courseRecordId, Long userId) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<RecordStudent>().eq(RecordStudent::getCourseRecordId, courseRecordId)
                .eq(RecordStudent::getUserId, userId));
    }

    @Override
    public void updatePass(Long recordStudentId, boolean isPass) {

        this.baseMapper.updatePass(recordStudentId, isPass);
    }
}
