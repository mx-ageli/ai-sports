package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.vo.CourseRecordVo;
import com.mx.ai.sports.course.vo.CourseVo;

/**
 * 课程记录Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ICourseRecordService extends IService<CourseRecord> {

    /**
     * 获取课程最新的记录Id
     *
     * @param courseId 课程Id
     * @return
     */
    Long findIdByNow(Long courseId);

    /**
     * 查询课程的记录
     * @param request
     * @param courseId
     * @return
     */
    IPage<CourseRecordVo> findByCourseId(QueryRequest request, Long courseId);

}
