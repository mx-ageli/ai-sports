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
    private IUserService userService;

    @Autowired
    private JPushConfigProperties jPushConfigProperties;

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

        Course course = getById(courseId);
        try {
            courseAddPush(1L, course);
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }

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
    public Boolean saveCourse(CourseUpdateVo updateVo, Long currentUserId) throws APIConnectionException, APIRequestException {
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
        }
        // 推送发布新课程的消息
        courseAddPush(currentUserId, course);
        // 更新课程任务
        return this.saveOrUpdate(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCourse(Course course, Long currentUserId) {
        // 不管课程是什么状态，都先把以前的课程任务删除
        jobService.deleteJobs(new String[]{course.getCourseJobId().toString(), course.getStudentJobId().toString()});

        // 如果课程状态为开启状态，就创建定时任务
        if (Objects.equals(course.getStatus(), Job.ScheduleStatus.NORMAL.getValue())) {
            // 创建课程相关的定时任务
            course.setCourseJobId(jobService.createCourseRecordJob(course));
            // 创建同步学生报名列表的任务
            course.setStudentJobId(jobService.createRecordStudentJob(course));
        }

        // 更新课程信息
        return this.saveOrUpdate(course);
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

    /**
     * 老师创建课程后给当前学校的所有学生推送消息
     *
     * @param userId 老师Id
     * @param course 课程信息
     * @return
     */
    private Boolean courseAddPush(Long userId, Course course) throws APIConnectionException, APIRequestException {
        ClientConfig config = ClientConfig.getInstance();

        JPushClient jpushClient = new JPushClient(jPushConfigProperties.getMasterSecret(), jPushConfigProperties.getAppKey(), null, config);

        User user = userService.getById(userId);
        // 查询这个老师对应的学校下面所有的学生
        List<String> deviceIds = userService.findStudentDeviceBySchoolId(user.getSchoolId());

        Map<String, String> extras = new HashMap<>(3);
        extras.put("courseId", course.getCourseId().toString());
        extras.put("courseName", course.getCourseName());
        extras.put("userName", user.getFullName());

        String content = user.getFullName() + "老师，发布了<" + course.getCourseName() + ">课程，快去报名吧！";
        Message message = Message.newBuilder().setMsgContent(content)
                .setTitle("有新的课程发布啦！")
                .addExtras(extras).build();
        PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
                                .setAudience(Audience.all())
                .setMessage(message).build();


        PushResult result = jpushClient.sendPush(payload);
//        PushPayload pushPayload = PushPayload.newBuilder().setPlatform(Platform.all())
//                .setMessage(Message.content("有新的课程发布啦!!!"))
//                .setAudience(Audience.registrationId(deviceIds)).build();
//
//        PushResult result = jpushClient.sendPush(pushPayload);

        log.info("课程创建成功后，消息推送成功! userId：{}, courseId:{}, courseName:{}, result:{}", userId, course.getCourseId(), course.getCourseName(), result);

        return Boolean.TRUE;
    }
}
