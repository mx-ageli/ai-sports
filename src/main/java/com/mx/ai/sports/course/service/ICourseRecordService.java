package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.vo.CourseRecordVo;

/**
 * 课程记录Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ICourseRecordService extends IService<CourseRecord> {

    /**
     * 获取课程最新的记录Id
     * @param courseId
     * @return
     */
    Long findIdByNow(Long courseId);

    /**
     * 获取课程最新的记录Id
     * @param courseId
     * @return
     */
    CourseRecord findByNow(Long courseId);

    /**
     * 获取课程最新的记录Id，如果没有获取到就创建
     *
     * @param courseId 课程Id
     * @return
     */
    Long findIdByNowAndCreate(Long courseId);

    /**
     * 查询课程的记录
     * @param request
     * @param courseId
     * @return
     */
    IPage<CourseRecordVo> findByCourseId(QueryRequest request, Long courseId);

    /**
     * 保存课程记录
     * @param courseId
     * @return
     */
    CourseRecord saveCourseRecord(Long courseId);



}
