package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.vo.RecordStudentVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;

import java.util.List;

/**
 * 课程记录中参加的学生Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface IRecordStudentService extends IService<RecordStudent> {

    /**
     * 根据课程记录Id查询已经参与的学生列表
     *
     * @param courseRecordId 课程记录Id
     * @return
     */
    List<Long> findUserIdByCourseRecordId(Long courseRecordId);

    /**
     * 根据课程记录Id查询学生的记录
     *
     * @param query 查询条件
     * @return
     */
    IPage<StudentCourseVo> findVoByCourseRecordId(StudentCourseQuery query);

    /**
     * 查询学生全部的上课记录
     *
     * @param request 分页条件
     * @return
     */
    IPage<RecordStudentVo> findRecordStudentVo(QueryRequest request, Long userId);

    /**
     *
     * @param courseRecordId
     * @param userId
     */
    RecordStudent findByCourseRecordIdAndUserId(Long courseRecordId, Long userId);

    void updatePass(Long recordStudentId, boolean isPass);

    void updateSigned(Long recordStudentId, boolean isAbsent, boolean isLate);

    Long getIdByCourseRecordIdUserId(Long courseRecordId, Long userId);
}
