package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.entity.Signed;
import com.mx.ai.sports.course.mapper.SignedMapper;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.course.service.ISignedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("SignedService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SignedServiceImpl extends ServiceImpl<SignedMapper, Signed> implements ISignedService {

    @Autowired
    private IRecordStudentService recordStudentService;

    @Autowired
    private ICourseRecordService courseRecordService;


    @Override
    public Signed findByCourseRecordId(Long courseRecordId, Long userId) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<Signed>().eq(Signed::getCourseRecordId, courseRecordId).eq(Signed::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSigned(Signed signed) {
        // 保存打卡记录
        Boolean isSuccess = this.saveOrUpdate(signed);
        // 只有在打上课卡的情况下才更新学生记录
        if(signed.getEndTime() == null){
            // 先查询学生的课程记录，正常情况是会有一条数据的，如果没有记录说明发生了异常，这里补录一条数据
            RecordStudent recordStudent = recordStudentService.findByCourseRecordIdAndUserId(signed.getCourseRecordId(), signed.getUserId());
            if (recordStudent == null) {
                recordStudent = new RecordStudent();
                recordStudent.setCourseRecordId(signed.getCourseRecordId());
                recordStudent.setCourseId(signed.getCourseId());
                recordStudent.setCreateTime(new Date());
                recordStudent.setUpdateTime(new Date());
                recordStudent.setUserId(signed.getUserId());
            }
            recordStudent.setUpdateTime(new Date());
            // 设置是否迟到
            recordStudent.setIsLate(signed.getIsLate());
            // 设置缺席，既然打卡成功了，所以就没有缺席
            recordStudent.setIsAbsent(false);
            // 更新学生的课程记录
            recordStudentService.saveOrUpdate(recordStudent);
            // 需要更新课程记录总表
            CourseRecord courseRecord = courseRecordService.getById(signed.getCourseRecordId());
            // 打卡人数加1
            courseRecord.setSingedCount(courseRecord.getSingedCount() + 1);
            // 缺席人数减1
            courseRecord.setAbsentCount(courseRecord.getAbsentCount() - 1);
            // 是否已经迟到
            if (signed.getIsLate()) {
                // 迟到人数加1
                courseRecord.setLateCount(courseRecord.getLateCount() + 1);
            }
            courseRecordService.saveOrUpdate(courseRecord);
        }
        return isSuccess;
    }

    @Override
    public Signed findLastByCourseId(Long courseId, Long userId) {
        Long courseRecordId = courseRecordService.findIdByNow(courseId);
        if(courseRecordId == null){
            return null;
        }
        return this.baseMapper.selectOne(new LambdaQueryWrapper<Signed>().eq(Signed::getCourseRecordId, courseRecordId).eq(Signed::getUserId, userId));
    }
}
