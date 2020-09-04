package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.mapper.CourseRecordMapper;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.vo.CourseRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements ICourseRecordService {

    @Override
    public Long findIdByNow(Long courseId) {
        // 查询课程最新的课程记录Id
        List<CourseRecord> courseRecords = this.baseMapper.selectList(new LambdaQueryWrapper<CourseRecord>().eq(CourseRecord::getCourseId, courseId).orderByDesc(CourseRecord::getCreateTime));
        if (CollectionUtils.isEmpty(courseRecords)) {
            return null;
        }
        // 返回第一条 最新的
        return courseRecords.get(0).getCourseRecordId();
    }

    @Override
    public IPage<CourseRecordVo> findByCourseId(QueryRequest request, Long courseId) {
        Page<CourseRecordVo> page = new Page<>(request.getPageNum(), request.getPageSize());

        return this.baseMapper.findByCourseId(page, courseId);
    }
}
