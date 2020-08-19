package com.mx.ai.sports.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.utils.DateUtil;
import com.mx.ai.sports.common.utils.SortUtil;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.job.entity.Job;
import com.mx.ai.sports.job.mapper.JobMapper;
import com.mx.ai.sports.job.service.IJobService;
import com.mx.ai.sports.job.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("JobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<Job> scheduleJobList = this.baseMapper.queryList();
        // 如果不存在，则创建
        scheduleJobList.forEach(scheduleJob -> {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        });
    }

    @Override
    public Job findJob(Long jobId) {
        return this.getById(jobId);
    }

    @Override
    public IPage<Job> findJobs(QueryRequest request, Job job) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(job.getBeanName())) {
            queryWrapper.eq(Job::getBeanName, job.getBeanName());
        }
        if (StringUtils.isNotBlank(job.getMethodName())) {
            queryWrapper.eq(Job::getMethodName, job.getMethodName());
        }
        if (StringUtils.isNotBlank(job.getParams())) {
            queryWrapper.like(Job::getParams, job.getParams());
        }
        if (StringUtils.isNotBlank(job.getRemark())) {
            queryWrapper.like(Job::getRemark, job.getRemark());
        }
        if (StringUtils.isNotBlank(job.getStatus())) {
            queryWrapper.eq(Job::getStatus, job.getStatus());
        }

        Page<Job> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", AiSportsConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJob(Job job) {
        job.setStatus(Job.ScheduleStatus.NORMAL.getValue());
        job.setCreateTime(new Date());
        this.save(job);
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 创建课程记录数据任务
     * @param course
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCourseRecordJob(Course course) {
        String beanName = "courseTask";
        String methodName = "courseRecordTask";

        LocalTime startLocalTime = LocalTime.parse(course.getSignedTime());
        // 在打卡时间的前五分钟创建
        startLocalTime = startLocalTime.minusMinutes(5);
        // 先创建课程记录数据任务
        return initJob(course, beanName, methodName, startLocalTime, " 创建课程记录数据任务");
    }

    /**
     * 初始化任务课程的
     *
     * @param course
     * @param beanName
     * @param methodName
     * @param time
     * @param remark
     */
    private Long initJob(Course course, String beanName, String methodName, LocalTime time, String remark) {
        String[] weeks = StringUtils.split(course.getWeek(), AiSportsConstant.SPLIT);
        String cron = DateUtil.getWeekCron(weeks, time);
        Job job = new Job();

        job.setBeanName(beanName);
        job.setMethodName(methodName);
        job.setCronExpression(cron);
        job.setRemark(course.getCourseName() + remark);
        job.setParams(course.getCourseId().toString());

        // 创建课程的定时任务
        createJob(job);

        return job.getJobId();
    }

    /**
     * 创建学生课程参加记录数据任务
     *
     * @param course
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRecordStudentJob(Course course) {
        String beanName = "courseTask";
        String methodName = "recordStudentTask";
        LocalTime startLocalTime = LocalTime.parse(course.getStartTime());
        // 开始时间时才创建
        return initJob(course, beanName, methodName, startLocalTime, " 创建学生课程参加记录数据任务");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Job job) {
        ScheduleUtils.updateScheduleJob(scheduler, job);
        this.baseMapper.updateById(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobs(String[] jobIds) {
        List<String> list = Arrays.asList(jobIds);
        list.forEach(jobId -> ScheduleUtils.deleteScheduleJob(scheduler, Long.valueOf(jobId)));
        this.baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(String jobIds, String status) {
        List<String> list = Arrays.asList(jobIds.split(StringPool.COMMA));
        Job job = new Job();
        job.setStatus(status);
        return this.baseMapper.update(job, new LambdaQueryWrapper<Job>().in(Job::getJobId, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.run(scheduler, this.findJob(Long.valueOf(jobId))));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.pauseJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, Job.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(String jobIds) {
        String[] list = jobIds.split(StringPool.COMMA);
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.resumeJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, Job.ScheduleStatus.NORMAL.getValue());
    }
}
