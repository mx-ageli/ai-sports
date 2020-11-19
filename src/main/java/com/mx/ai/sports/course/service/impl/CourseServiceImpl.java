package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.DateUtil;
import com.mx.ai.sports.course.converter.CourseConverter;
import com.mx.ai.sports.course.dto.ExportRecordStudentDto;
import com.mx.ai.sports.course.dto.ExportRecordTotalDto;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.mapper.CourseMapper;
import com.mx.ai.sports.course.query.CourseAddVo;
import com.mx.ai.sports.course.service.*;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import com.mx.ai.sports.job.entity.Job;
import com.mx.ai.sports.job.service.IJobService;
import com.mx.ai.sports.system.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mx.ai.sports.common.entity.AiSportsConstant.ENTRY_EXPIRE_TIME;

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

    @Autowired
    private ICourseStudentService courseStudentService;

    @Autowired
    private IRunService runService;

    @Autowired
    private ISignedService signedService;

    @Autowired
    private IGroupService groupService;


    @Override
    public Course findByCourseName(Long courseId, String courseName) {
        return baseMapper.findByCourseName(courseId, courseName);
    }

    @Override
    public IPage<CourseVo> findAll(QueryRequest request, Long userId) {
        Page<StudentCourseVo> page = new Page<>(request.getPageNum(), request.getPageSize());
        // 获取今天是星期几
        int week = DateUtil.getWeek();


        return this.baseMapper.findAll(page, week, userId);
    }

    @Override
    public CourseVo findById(Long courseId) {
        // 获取今天是星期几
        int week = DateUtil.getWeek();

        return this.baseMapper.findById(week, courseId);
    }

    @Override
    public IPage<CourseVo> findMyEntry(QueryRequest request, Long userId) {
        Page<CourseVo> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 获取今天是星期几
        int week = DateUtil.getWeek();

        return this.baseMapper.findMyEntry(page, week, userId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCourse(CourseAddVo addVo, Long currentUserId) throws AiSportsException {
        Course course = courseConverter.addVo2Domain(addVo);
        course.setUserId(currentUserId);
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());

        course.setMaxCount(addVo.getMaxCount());
        course.setGroupCount(addVo.getGroupCount());

        // 如果课程状态为空，默认为开启
        if (StringUtils.isBlank(addVo.getStatus())) {
            course.setStatus(Job.ScheduleStatus.NORMAL.getValue());
        } else {
            course.setStatus(addVo.getStatus());
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
//            course.setStartJobId(jobService.createCourseStartJob(course));
            // 创建课程结束后删除所有的报名学生任务
            course.setFinishJobId(jobService.deleteCourseStudentJob(course));
        }
        // 创建课程小组
        groupService.batchCreate(course, addVo.getGroupCount(), addVo.getMaxCount());

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
//            course.setStartJobId(jobService.createCourseStartJob(course));
            // 创建课程结束后删除所有的报名学生任务
            course.setFinishJobId(jobService.deleteCourseStudentJob(course));
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
        String[] jobIds = new String[4];
        if (Objects.nonNull(course.getCourseJobId())) {
            jobIds[0] = Objects.toString(course.getCourseJobId());
        }
        if (Objects.nonNull(course.getStudentJobId())) {
            jobIds[1] = Objects.toString(course.getStudentJobId());
        }
        if (Objects.nonNull(course.getStartJobId())) {
            jobIds[2] = Objects.toString(course.getStartJobId());
        }
        if (Objects.nonNull(course.getFinishJobId())) {
            jobIds[3] = Objects.toString(course.getFinishJobId());
        }

        // 不管课程是什么状态，都先把以前的课程任务删除
        jobService.deleteJobs(jobIds);
    }

    @Override
    public List<CourseVo> findMyEntryByCurrent(Long currentUserId) {

        int week = DateUtil.getWeek();
        // 当前时间
        LocalTime localTime = LocalTime.now();
        // 查询到今天我报名的课程
        List<CourseVo> courseVos = this.baseMapper.findMyEntryByCurrent(week, currentUserId);

        return courseVos.stream().filter(e -> localTime.isAfter(LocalTime.parse(e.getStartTime())) && localTime.isBefore(LocalTime.parse(e.getEndTime()))).collect(Collectors.toList());
    }

    @Override
    public List<ExportRecordTotalDto> findExportRecordTotal(Date startTime, Date endTime) {

        List<ExportRecordTotalDto> exportRecordTotalDtos = this.baseMapper.findExportRecordTotal(startTime, endTime);
        // 查询当前各个课程报名的人数
        Map<Long, Long> studentCountMap = courseStudentService.findCourseStudentCount();
        // 时间内累计的跑步次数
        Map<Long, Long> runCountMap = runService.findCourseRunCount(startTime, endTime);

        for (ExportRecordTotalDto dto : exportRecordTotalDtos) {
            dto.setCurrentCount(studentCountMap.getOrDefault(dto.getCourseId(), 0L));
            dto.setRunCount(runCountMap.getOrDefault(dto.getCourseId(), 0L));
        }

        return exportRecordTotalDtos;
    }

    @Override
    public Map<String, List<ExportRecordStudentDto>> findExportRecordStudent(Date startTime, Date endTime) {

        List<ExportRecordStudentDto> studentVos = this.baseMapper.findExportRecordStudent(startTime, endTime);

        if (CollectionUtils.isEmpty(studentVos)) {
            return new HashMap<>(0);
        }
        // 课程下学生对应的打卡数量
        Map<Long, Map<Long, Long>> signedCountMap = signedService.findCourseSignedCount(startTime, endTime);

        for (ExportRecordStudentDto dto : studentVos) {
            Long signedCount = 0L;
            if (signedCountMap.containsKey(dto.getCourseId())) {
                signedCount = signedCountMap.get(dto.getCourseId()).getOrDefault(dto.getUserId(), 0L);
            }

            dto.setSignedCount(signedCount);
        }

        return studentVos.stream().collect(Collectors.groupingBy(ExportRecordStudentDto::getCourseName));
    }


}
