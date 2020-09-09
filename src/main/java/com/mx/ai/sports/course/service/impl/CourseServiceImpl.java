package com.mx.ai.sports.course.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.configure.JPushConfigProperties;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.converter.CourseConverter;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.mapper.CourseMapper;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import com.mx.ai.sports.job.entity.Job;
import com.mx.ai.sports.job.service.IJobService;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IMessageService;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseConverter courseConverter;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IMessageService messageService;

    @Override
    public Course findByCourseName(Long courseId, String courseName) {
        return baseMapper.findByCourseName(courseId, courseName);
    }

    @Override
    public IPage<CourseVo> findAll(QueryRequest request, Long userId) {
        Page<StudentCourseVo> page = new Page<>(request.getPageNum(), request.getPageSize());
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;


        return this.baseMapper.findAll(page, week, userId);
    }

    @Override
    public CourseVo findById(Long courseId) {
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;

        return this.baseMapper.findById(week, courseId);
    }

    @Override
    public IPage<CourseVo> findMyEntry(QueryRequest request, Long userId) {
        Page<CourseVo> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;

        return this.baseMapper.findMyEntry(page, week, userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCourse(CourseUpdateVo updateVo, Long currentUserId) throws AiSportsException {
        Course course = courseConverter.vo2Domain(updateVo);
        course.setUserId(currentUserId);
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());

        // 如果课程状态为空，默认为开启
        if (StringUtils.isBlank(updateVo.getStatus())) {
            course.setStatus(Job.ScheduleStatus.NORMAL.getValue());
        } else {
            course.setStatus(updateVo.getStatus());
        }
        // 先保存课程数据
        this.save(course);
        // 如果课程状态为开启状态，就创建定时任务
        if (Objects.equals(course.getStatus(), Job.ScheduleStatus.NORMAL.getValue())) {
            // 创建课程相关的定时任务
            course.setCourseJobId(jobService.createCourseRecordJob(course));
            // 创建同步学生报名列表的任务
            course.setStudentJobId(jobService.createRecordStudentJob(course));
            // 创建课程开始前的消息推送任务
            course.setStartJobId(jobService.createCourseStartJob(course));

        }
        // 推送发布新课程的消息
        messageService.courseAddPush(currentUserId, course);
        // 更新课程任务
        return this.saveOrUpdate(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCourse(Course course, Long currentUserId) {
        // 先删除任务
        deleteJobs(course);
        // 如果课程状态为开启状态，就创建定时任务
        if (Objects.equals(course.getStatus(), Job.ScheduleStatus.NORMAL.getValue())) {
            // 创建课程相关的定时任务
            course.setCourseJobId(jobService.createCourseRecordJob(course));
            // 创建同步学生报名列表的任务
            course.setStudentJobId(jobService.createRecordStudentJob(course));
            // 创建课程开始前的消息推送任务
            course.setStartJobId(jobService.createCourseStartJob(course));
        }
        // 更新课程信息
        return this.saveOrUpdate(course);
    }

    /**
     * 先删除任务
     *
     * @param course
     */
    private void deleteJobs(Course course) {
        String[] jobIds = new String[3];
        if (Objects.nonNull(course.getCourseJobId())) {
            jobIds[0] = Objects.toString(course.getCourseJobId());
        }
        if (Objects.nonNull(course.getStudentJobId())) {
            jobIds[1] = Objects.toString(course.getStudentJobId());
        }
        if (Objects.nonNull(course.getStartJobId())) {
            jobIds[2] = Objects.toString(course.getStartJobId());
        }

        // 不管课程是什么状态，都先把以前的课程任务删除
        jobService.deleteJobs(jobIds);
    }

    @Override
    public List<CourseVo> findMyEntryByCurrent(Long currentUserId) {

        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;
        // 当前时间
        LocalTime localTime = LocalTime.now();
        // 查询到今天我报名的课程
        List<CourseVo> courseVos = this.baseMapper.findMyEntryByCurrent(week, currentUserId);

        return courseVos.stream().filter(e -> localTime.isAfter(LocalTime.parse(e.getStartTime())) && localTime.isBefore(LocalTime.parse(e.getEndTime()))).collect(Collectors.toList());
    }

}
