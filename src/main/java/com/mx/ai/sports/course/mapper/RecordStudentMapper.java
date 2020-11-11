package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.vo.RecordStudentVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
public interface RecordStudentMapper extends BaseMapper<RecordStudent> {

    /**
     *
     * @param page
     * @param courseRecordId
     * @param type
     * @return
     */
    IPage<StudentCourseVo> findVoByCourseRecordId(Page<StudentCourseVo> page, Long courseRecordId, String type);

    /**
     *
     * @param page
     * @param userId
     * @return
     */
    IPage<RecordStudentVo> findRecordStudentVo(Page<RecordStudentVo> page, Long userId);

    void updatePass(Long recordStudentId, boolean isPass);

    void updateSigned(Long recordStudentId, boolean isAbsent, boolean isLate);

    void updateSignedToGone(Long recordStudentId, boolean isGone);

    Long getIdByCourseRecordIdUserId(Long courseRecordId, Long userId);

}