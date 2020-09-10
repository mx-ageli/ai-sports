package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.mapper.CourseRecordMapper;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.vo.CourseRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements ICourseRecordService {

    @Autowired
    private ICourseStudentService courseStudentService;

    @Override
    public Long findIdByNow(Long courseId) {
        CourseRecord courseRecord = baseMapper.findIdByNow(courseId);
        if(courseRecord == null){
            return null;
        }
        return courseRecord.getCourseRecordId();
    }

    @Override
    public Long findIdByNowAndCreate(Long courseId) {
        CourseRecord courseRecord = baseMapper.findIdByNow(courseId);
        // 如果没有查询到，说明这种情况是没有执行定时任务去创建的
        if(courseRecord == null){
            // 这里默认去创建
            courseRecord = saveCourseRecord(courseId);
        }

        return courseRecord.getCourseRecordId();
    }

    @Override
    public IPage<CourseRecordVo> findByCourseId(QueryRequest request, Long courseId) {
        Page<CourseRecordVo> page = new Page<>(request.getPageNum(), request.getPageSize());

        return this.baseMapper.findByCourseId(page, courseId);
    }

    @Override
    public CourseRecord saveCourseRecord(Long courseId) {
        // 创建课程记录
        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setCreateTime(new Date());
        courseRecord.setCourseId(courseId);

        // 现将报名的总人数先统计到课程记录表中
        // 统计课程报名的总人数， 这里有可能没有学生报名这个课程，就为0
        List<Long> userIds = courseStudentService.findByCourseId(courseId);
        Long allCount = (long) userIds.size();
        courseRecord.setAllCount(allCount);
        // 默认认为所有人都是缺席
        courseRecord.setAbsentCount(allCount);

        this.save(courseRecord);

        return courseRecord;
    }
}
