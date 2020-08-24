package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.vo.CourseRecordVo;

/**
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
public interface CourseRecordMapper extends BaseMapper<CourseRecord> {

    /**
     *
     * @param page
     * @param courseId
     * @return
     */
    IPage<CourseRecordVo> findByCourseId(Page<CourseRecordVo> page, Long courseId);
}