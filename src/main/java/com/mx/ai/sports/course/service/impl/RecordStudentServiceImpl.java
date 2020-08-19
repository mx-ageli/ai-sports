package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.mapper.RecordStudentMapper;
import com.mx.ai.sports.course.service.IRecordStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("RecordStudentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RecordStudentServiceImpl extends ServiceImpl<RecordStudentMapper, RecordStudent> implements IRecordStudentService {


    @Override
    public List<Long> findByCourseId(Long courseId) {
        List<RecordStudent> recordStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<RecordStudent>().eq(RecordStudent::getCourseId, courseId));
        if(CollectionUtils.isEmpty(recordStudentList)){
            return new ArrayList<>();
        }
        return recordStudentList.stream().map(RecordStudent::getUserId).collect(Collectors.toList());
    }
}
