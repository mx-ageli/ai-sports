package com.mx.ai.sports.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 课程的任务调度
 * @author Mengjiaxin
 * @date 2020/8/17 10:26 下午
 */
@Slf4j
@Component
public class CourseTask {

    /**
     * 课程活动开始创建课程记录数据， 在打卡时间开始前5分钟创建
     * 根据课程Id创建课程记录
     * CourseRecord表
     * @param courseId
     */
    public void courseRecordTask(String courseId){
        // 一个课程活动在一天内只可能创建一次课程记录， 需要判断同一天是否已经创建了课程记录

        // 创建课程记录

        log.info("我是带参数的courseRecordTask方法，正在被执行，参数为：{}" , courseId);
    }

    /**
     * 在课程开始时，同步报名学生数据。
     * 这个时候部分报名学生数据会在学生参加记录表中存在，只同步不存在的学生数据
     * CourseStudent表 -> RecordStudent表
     *
     * @param courseId
     */
    public void courseStudentTask(Long courseId){

        // 根据课程Id查询最新的课程记录，如果没有查询到，说明这次课程记录创建出现问题

        // 开始同步报名的学生到学生记录表中

        // 已经打卡的学生会在课程记录中存在，这种数据无需再次添加



    }





}
