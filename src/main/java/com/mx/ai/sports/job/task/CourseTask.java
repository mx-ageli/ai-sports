package com.mx.ai.sports.job.task;

import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.system.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 课程的任务调度
 *
 * @author Mengjiaxin
 * @date 2020/8/17 10:26 下午
 */
@Slf4j
@Component
public class CourseTask {


    @Autowired
    private ICourseRecordService courseRecordService;

    @Autowired
    private ICourseStudentService courseStudentService;

    @Autowired
    private IRecordStudentService recordStudentService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IMessageService messageService;

    /**
     * 课程活动开始创建课程记录数据， 在打卡时间开始前5分钟创建
     * 根据课程Id创建课程记录
     * CourseRecord表
     *
     * @param courseId
     */
    public void courseRecordTask(String courseId) {
        // 创建课程记录
        CourseRecord courseRecord = courseRecordService.saveCourseRecord(Long.valueOf(courseId));

        log.info("创建课程记录，courseId:{}，courseRecordId:{}", courseId, courseRecord.getCourseRecordId());
    }

    /**
     * 在课程开始时，同步报名学生数据。
     * 这个时候部分报名学生数据会在学生参加记录表中存在，只同步不存在的学生数据
     * CourseStudent表 -> RecordStudent表
     *
     * @param courseId
     */
    public void recordStudentTask(String courseId) {
        Long longCourseId = Long.valueOf(courseId);
        // 课程记录Id
        Long courseRecordId = courseRecordService.findIdByNowAndCreate(longCourseId);
        // 本次课程报名的所有学生
        List<Long> studentIds = courseStudentService.findByCourseId(longCourseId);
        // 本次课程已经参与打卡的学生 已经打卡的学生会在课程记录中存在，这种数据无需再次添加
        List<Long> currentStudentIds = recordStudentService.findUserIdByCourseRecordId(courseRecordId);
        // 先将已经参与打卡的学生删除掉
        studentIds.removeAll(currentStudentIds);

        List<RecordStudent> recordStudentList = new ArrayList<>();
        for (Long studentId : studentIds) {
            RecordStudent recordStudent = new RecordStudent(longCourseId, courseRecordId, studentId);

            recordStudentList.add(recordStudent);
        }
        recordStudentService.saveBatch(recordStudentList);

        log.info("同步报名学生数据，courseId:{}", courseId);
    }

    /**
     * 课程开始前的消息推送
     *
     * @param courseId
     */
    public void courseStartTask(String courseId) {
        Course course = courseService.getById(courseId);
        try {
            messageService.courseStartPush(course);
        } catch (AiSportsException e) {
            log.error("课程开始前的消息推送发生异常！异常:{}", e.getMessage());
        }
        log.info("课程开始前的消息推送成功，courseId:{}", courseId);
    }


}
