package com.mx.ai.sports.job.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mx.ai.sports.common.entity.RunStatusEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.*;
import com.mx.ai.sports.course.service.*;
import com.mx.ai.sports.course.vo.CountVo;
import com.mx.ai.sports.system.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired
    private IGroupStudentService groupStudentService;

    @Autowired
    private ISignedService signedService;

    @Autowired
    private IRunService runService;

    @Autowired
    private IKeepService keepService;

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

    /**
     * 课程结束后删除所有的报名学生任务
     *
     * @param courseId
     */
    public void deleteCourseStudentTask(String courseId) {
        // 统计课程的打卡数量、迟到数量、缺席数量、合格数量、不合格数量
        CourseRecord courseRecord = courseRecordService.findByNow(Long.valueOf(courseId));
        // 查询出签到的人和迟到人数量
        List<CountVo> signedCountList = signedService.findCountByCourseRecordId(courseRecord.getCourseRecordId());
        for (CountVo countVo : signedCountList) {
            if ("0".equals(countVo.getKey())) {
                courseRecord.setSingedCount(countVo.getCount());
            } else if ("1".equals(countVo.getKey())) {
                courseRecord.setLateCount(countVo.getCount());
            }
        }
        // 缺席的人等于 全部人-签到的人-迟到的人
        courseRecord.setAbsentCount(courseRecord.getAllCount() - courseRecord.getSingedCount() - courseRecord.getLateCount());

        List<CountVo> runCountList = runService.findCountByCourseRecordId(courseRecord.getCourseRecordId());
        if (CollectionUtils.isNotEmpty(runCountList)) {
            setCourseRecord(courseRecord, runCountList);
        }

        List<CountVo> keepCountList = keepService.findCountByCourseRecordId(courseRecord.getCourseRecordId());
        if (CollectionUtils.isNotEmpty(keepCountList)) {
            setCourseRecord(courseRecord, keepCountList);
        }
        // 重新保存统计数据
        courseRecordService.saveOrUpdate(courseRecord);

        int delCount = courseStudentService.removeByCourseId(courseId);
        // 还需要将学生与课程的小组关系删除
        groupStudentService.remove(new LambdaQueryWrapper<GroupStudent>().eq(GroupStudent::getCourseId, courseId));
        // 清除报名学生的缓存
        courseStudentService.removeEntryStudentList2Redis(Long.valueOf(courseId));
        log.info("课程结束，清除所有的学生报名记录，courseId:{}, 清除数量：{}", courseId, delCount);
    }

    /**
     * 健身课程的赋值
     *
     * @param courseRecord
     * @param runCountList
     */
    private void setCourseRecord(CourseRecord courseRecord, List<CountVo> runCountList) {
        for (CountVo countVo : runCountList) {
            if (RunStatusEnum.PASS.value().equals(countVo.getKey())) {
                courseRecord.setPassCount(countVo.getCount());
            } else if (RunStatusEnum.NO_PASS.value().equals(countVo.getKey())) {
                courseRecord.setNoPassCount(countVo.getCount());
            }
        }
    }


}
