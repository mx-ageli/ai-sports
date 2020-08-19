package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.entity.RecordStudent;

import java.util.List;

/**
 * 课程记录中参加的学生Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface IRecordStudentService extends IService<RecordStudent> {

    /**
     * 根据课程Id查询已经参与的学生列表
     *
     * @param courseId 课程Id
     * @return
     */
    List<Long> findByCourseId(Long courseId);
}
