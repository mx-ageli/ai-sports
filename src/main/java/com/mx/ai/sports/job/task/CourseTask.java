package com.mx.ai.sports.job.task;

import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.entity.RecordStudent;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.service.IRecordStudentService;
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

    /**
     * 课程活动开始创建课程记录数据， 在打卡时间开始前5分钟创建
     * 根据课程Id创建课程记录
     * CourseRecord表
     *
     * @param courseId
     */
    public void courseRecordTask(String courseId) {
        // 创建课程记录
        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setCreateTime(new Date());
        courseRecord.setCourseId(Long.valueOf(courseId));

        // 现将报名的总人数先统计到课程记录表中
        // 统计课程报名的总人数， 这里有可能没有学生报名这个课程，就为0
        List<Long> userIds = courseStudentService.findByCourseId(Long.valueOf(courseId));
        Long allCount = (long) userIds.size();
        courseRecord.setAllCount(allCount);
        // 默认认为所有人都是缺席
        courseRecord.setAbsentCount(allCount);

        courseRecordService.save(courseRecord);

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
        Long courseRecordId = courseRecordService.findIdByNew(longCourseId);
        // 本次课程报名的所有学生
        List<Long> studentIds = courseStudentService.findByCourseId(longCourseId);
        // 本次课程已经参与打卡的学生 已经打卡的学生会在课程记录中存在，这种数据无需再次添加
        List<Long> currentStudentIds = recordStudentService.findUserIdByCourseRecordId(courseRecordId);
        // 先将已经参与打卡的学生删除掉
        studentIds.removeAll(currentStudentIds);

        List<RecordStudent> recordStudentList = new ArrayList<>();
        for (Long studentId : studentIds) {
            RecordStudent recordStudent = new RecordStudent();
            recordStudent.setCourseId(longCourseId);
            recordStudent.setCourseRecordId(courseRecordId);
            recordStudent.setUserId(studentId);
            recordStudent.setCreateTime(new Date());
            recordStudent.setUpdateTime(new Date());
            // 默认缺席
            recordStudent.setIsAbsent(Boolean.TRUE);
            // 默认迟到
            recordStudent.setIsLate(Boolean.TRUE);
            recordStudentList.add(recordStudent);
        }
        recordStudentService.saveBatch(recordStudentList);

        log.info("同步报名学生数据，courseId:{}", courseId);
    }


}
