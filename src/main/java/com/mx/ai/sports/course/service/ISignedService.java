package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.course.dto.CourseSignedCountDto;
import com.mx.ai.sports.course.entity.Signed;

import java.util.Date;
import java.util.Map;

/**
 * 打卡Service
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
public interface ISignedService extends IService<Signed> {

    /**
     * 根据课程记录Id查询用户的打卡记录
     *
     * @param courseRecordId 课程记录Id
     * @param userId         用户Id
     * @return
     */
    Signed findByCourseRecordId(Long courseRecordId, Long userId);


    /**
     * 保存打卡记录
     * @param signed
     * @return
     */
    Boolean saveSigned(Signed signed);

    /**
     * 查询用户最近的打卡记录
     * @param courseId
     * @param userId
     * @return
     */
    Signed findLastByCourseId(Long courseId, Long userId);

    /**
     * 查询时间内,各个课程下各个学生的累计的打卡次数
     * @param startTime
     * @param endTime
     * @return
     */
    Map<Long, Map<Long, Long>> findCourseSignedCount(Date startTime, Date endTime);
}
