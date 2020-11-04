package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.dto.CourseRunCountDto;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.vo.CountVo;
import com.mx.ai.sports.course.vo.RunRecordDetailVo;
import com.mx.ai.sports.course.vo.RunRecordVo;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/24 5:22 下午
 */
public interface RunMapper extends BaseMapper<Run> {


    RunRecordVo getRunRecordCountVo(Long userId, Date startTime, Date endTime);

    IPage<RunRecordDetailVo> findRunRecordDetailVo(Page<RunRecordDetailVo> page, Long userId, Date startTime, Date endTime, String status);

    List<CourseRunCountDto> findCourseRunCount(Date startTime, Date endTime);

    List<CountVo> findCountByCourseRecordId(Long courseRecordId);

    Run findByCourseRecordUserId(Long courseRecordId, Long userId);
}