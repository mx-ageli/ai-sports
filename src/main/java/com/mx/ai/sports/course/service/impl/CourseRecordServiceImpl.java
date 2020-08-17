package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.mapper.CourseRecordMapper;
import com.mx.ai.sports.course.service.ICourseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseRecordService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseRecordServiceImpl extends ServiceImpl<CourseRecordMapper, CourseRecord> implements ICourseRecordService {


}
