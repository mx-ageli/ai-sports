package com.mx.ai.sports.job.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.job.entity.Job;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
public interface IJobService extends IService<Job> {

    Job findJob(Long jobId);

    IPage<Job> findJobs(QueryRequest request, Job job);

    void createJob(Job job);

    /**
     * 创建课程记录数据任务
     * @param course
     * @return
     */
    Long createCourseRecordJob(Course course);

    /**
     * 创建同步学生报名列表的任务
     * @param course
     * @return
     */
    Long createRecordStudentJob(Course course);

    /**
     * 课程开始前的消息推送
     * @param course
     * @return
     */
    Long createCourseStartJob(Course course);

    /**
     * 课程结束后删除所有的报名学生
     * @param course
     * @return
     */
    Long deleteCourseStudentJob(Course course);


    void updateJob(Job job);

    void deleteJobs(String[] jobIds);

    int updateBatch(String jobIds, String status);

    void run(String jobIds);

    void pause(String jobIds);

    void resume(String jobIds);

}
