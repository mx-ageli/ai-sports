package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.course.dto.CourseSignedCountDto;
import com.mx.ai.sports.course.entity.Signed;
import com.mx.ai.sports.course.vo.CountVo;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/24 5:22 下午
 */
public interface SignedMapper extends BaseMapper<Signed> {


    List<CourseSignedCountDto> findCourseSignedCount(Date startTime, Date endTime);

    List<CountVo> findCountByCourseRecordId(Long courseRecordId);

}