package com.mx.ai.sports.app.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.CourseApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.EntryEnum;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.converter.CourseConverter;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.query.CourseQuery;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.query.UserCourseQuery;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.service.IRecordStudentService;
import com.mx.ai.sports.course.vo.*;
import com.mx.ai.sports.job.entity.Job;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.UserSimple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 课程相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("CourseApi")
public class CourseController extends BaseRestController implements CourseApi {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICourseRecordService courseRecordService;

    @Autowired
    private IRecordStudentService recordStudentService;

    @Autowired
    private ICourseStudentService courseStudentService;

    @Autowired
    private IUserService userService;

    @Override
    @TeacherRole
    @Log("查询我发布的课程列表")
    public AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest request) throws AiSportsException {
        return new AiSportsResponse<IPage<CourseVo>>().success().data(courseService.findAll(request, getCurrentUserId()));
    }

    @Override
    @TeacherRole
    @Log("老师创建课程")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid CourseUpdateVo updateVo) throws AiSportsException {
        // 校验课程名称是否重复
        checkCourseName(null, updateVo.getCourseName());
        // 校验课程时间数据的范围校验
        checkUpdateCourseTime(updateVo);

        // 老师新增课程，并创建定时任务
        courseService.saveCourse(updateVo, getCurrentUserId());

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    /**
     * 校验课程名称是否重复
     *
     * @param courseName 课程名称
     * @throws AiSportsException
     */
    private void checkCourseName(Long courseId, String courseName) throws AiSportsException {
        // 判断课程名称是否重复
        Course checkCourseName = courseService.findByCourseName(courseId, courseName);
        if (checkCourseName != null) {
            throw new AiSportsException("课程名称已存在，请重新编辑！");
        }
    }

    /**
     * 校验修改的课程时间数据的范围校验
     *
     * @param updateVo
     * @throws AiSportsException
     */
    private void checkUpdateCourseTime(CourseUpdateVo updateVo) throws AiSportsException {
        // 还需要校验 开始时间结束时间的范围差值
        // 开始时间
        LocalTime startTime = LocalTime.parse(updateVo.getStartTime());
        // 结束时间
        LocalTime endTime = LocalTime.parse(updateVo.getEndTime());
        // 打卡时间
        LocalTime signedTime = LocalTime.parse(updateVo.getSignedTime());
        // 开始时间必须在结束时间之前
        // startTime < endTime = true
        if (startTime.isAfter(endTime)) {
            throw new AiSportsException("开始时间必须在结束时间之前!");
        }
        // 打卡时间必须在开始时间之前
        // signedTime < startTime = true
        if (signedTime.isAfter(startTime)) {
            throw new AiSportsException("打卡时间必须在开始时间之前!");
        }
    }

    @Override
    @TeacherRole
    @Log("老师修改课程")
    public AiSportsResponse<Boolean> update(@RequestBody @Valid CourseUpdateVo updateVo) throws AiSportsException {
        Course course = courseService.getById(updateVo.getCourseId());
        if (course == null) {
            throw new AiSportsException("课程Id不存在，没有查询到数据!");
        }

        // 校验课程名称是否重复, 排除当前课程
        checkCourseName(updateVo.getCourseId(), updateVo.getCourseName());
        // 校验课程时间数据的范围校验
        checkUpdateCourseTime(updateVo);
        // 校验当前时间节点是否有已经开始的课程在进行，如果有不能进行修改
        if (isCheckStart(course.getWeek(), course.getStartTime(), course.getEndTime())) {
            throw new AiSportsException("当前课程正在进行中，不能修改! 请等本次课程结束后再修改！");
        }
        Long userId = getCurrentUserId();

        course.setUserId(userId);
        course.setCourseName(updateVo.getCourseName());
        course.setUpdateTime(new Date());
        course.setWeek(updateVo.getWeek());
        course.setStartTime(updateVo.getStartTime());
        course.setEndTime(updateVo.getEndTime());
        course.setSignedTime(updateVo.getSignedTime());
        course.setLat(updateVo.getLat());
        course.setLon(updateVo.getLon());
        course.setLocationName(updateVo.getLocationName());
        course.setScope(updateVo.getScope());
        course.setImages(updateVo.getImages());
        course.setContent(updateVo.getContent());
        course.setIsRun(updateVo.getIsRun());
        if (StringUtils.isNotBlank(updateVo.getStatus())) {
            course.setStatus(updateVo.getStatus());
        }
        // 更新课程信息
        return new AiSportsResponse<Boolean>().success().data(courseService.updateCourse(course, userId));
    }

    @Override
    @TeacherRole
    @Log("查询某一个课程报名数量")
    public AiSportsResponse<CourseNumVo> findNumById(@NotNull @RequestParam("courseRecordId") Long courseRecordId) {
        CourseRecord courseRecord = courseRecordService.getById(courseRecordId);
        if (courseRecord == null) {
            return new AiSportsResponse<CourseNumVo>().fail().message("课程记录Id不存在，没有查询到数据!");
        }

        CourseNumVo numVo = new CourseNumVo();
        numVo.setAll(courseRecord.getAllCount());
        numVo.setAbsent(courseRecord.getAbsentCount());
        numVo.setNoPass(courseRecord.getNoPassCount());
        numVo.setPass(courseRecord.getPassCount());

        return new AiSportsResponse<CourseNumVo>().success().data(numVo);
    }

    @Override
    @TeacherRole
    @Log("查询某一个课程的完成情的学生列表")
    public AiSportsResponse<IPage<StudentCourseVo>> findStudentById(@RequestBody @Valid StudentCourseQuery query) throws AiSportsException {
        return new AiSportsResponse<IPage<StudentCourseVo>>().success().data(recordStudentService.findVoByCourseRecordId(query));
    }

    @Override
    @Log("查询所有的课程列表")
    public AiSportsResponse<IPage<CourseVo>> findAll(@RequestBody @Valid QueryRequest request) {
        UserSimple user = getCurrentUser();
        // 直接查询全部数据返回
        if (Objects.equals(user.getRoleId(), RoleEnum.TEACHER.value())) {
            return new AiSportsResponse<IPage<CourseVo>>().success().data(courseService.findAll(request, null));
        } else {
            IPage<CourseVo> coursePage = courseService.findAll(request, null);
            // 学生需要遍历当前课程的状态 课程报名状态，1可报课 2不可报 3已报课
            // 我已经报名的课程列表
            List<Long> courseIds = courseStudentService.findByUserId(user.getUserId());

            for (CourseVo courseVo : coursePage.getRecords()) {
                // 赋值课程状态
                setEntryStatus(courseIds, courseVo);
            }
            return new AiSportsResponse<IPage<CourseVo>>().success().data(coursePage);
        }
    }

    /**
     * 赋值课程状态
     *
     * @param courseIds
     * @param courseVo
     */
    private void setEntryStatus(List<Long> courseIds, CourseVo courseVo) {
        // 默认为可以报课
        courseVo.setEntryStatus(EntryEnum.OK.value());
        // 如果课程已经开始了，设置状态为不可报名
        if (isCheckStart(courseVo.getWeek(), courseVo.getStartTime(), courseVo.getEndTime())) {
            courseVo.setEntryStatus(EntryEnum.NO.value());
        }
        // 判断当前用户是否已经报名这个课程
        if (courseIds.contains(courseVo.getCourseId())) {
            courseVo.setEntryStatus(EntryEnum.ENTRY.value());
        }
        // 判断课程是否暂停状态
        if (Objects.equals(courseVo.getStatus(), Job.ScheduleStatus.PAUSE.getValue())) {
            courseVo.setEntryStatus(EntryEnum.FINISH.value());
        }
    }

    @Override
    @Log("查询我已经报名的课程")
    public AiSportsResponse<IPage<CourseVo>> findMyEntry(@RequestBody @Valid QueryRequest request) {
        return new AiSportsResponse<IPage<CourseVo>>().success().data(courseService.findMyEntry(request, getCurrentUserId()));
    }

    @Override
    @Log("查询课程详细信息")
    public AiSportsResponse<CourseVo> findById(@NotNull @RequestParam("courseId") Long courseId) {

        UserSimple user = getCurrentUser();
        CourseVo courseVo = courseService.findById(courseId);
        // 直接查询全部数据返回
        if (Objects.equals(user.getRoleId(), RoleEnum.TEACHER.value())) {
            return new AiSportsResponse<CourseVo>().success().data(courseVo);
        }
        // 我已经报名的课程列表
        List<Long> courseIds = courseStudentService.findByUserId(user.getUserId());
        // 赋值课程状态
        setEntryStatus(courseIds, courseVo);
        return new AiSportsResponse<CourseVo>().success().data(courseVo);
    }

    @Override
    @Log("学生报名课程")
    public AiSportsResponse<Boolean> entry(@NotNull @RequestParam("courseId") Long courseId) {

        Course course = courseService.getById(courseId);
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }
        // 课程是否已经开始
        boolean isCheckStart = isCheckStart(course.getWeek(), course.getStartTime(), course.getEndTime());
        Long userId = getCurrentUserId();
        // 查询学生是否报名
        CourseStudent courseStudent = courseStudentService.findByUserCourseId(userId, courseId);
        // 如果已经报名了，就删除报名信息，取消报名
        if (courseStudent != null) {
            if (isCheckStart) {
                return new AiSportsResponse<Boolean>().fail().message("当前课程正在进行中，不能取消报名！请等课程结束后再试！");
            }
            return new AiSportsResponse<Boolean>().success().data(courseStudentService.remove(userId, courseId));
        } else {  // 没有报名的话就报名
            if (isCheckStart) {
                return new AiSportsResponse<Boolean>().fail().message("当前报名课程已经开始，不能报名！请等课程结束后再试！");
            }
            courseStudent = new CourseStudent();
            courseStudent.setCourseId(courseId);
            courseStudent.setUserId(userId);

            return new AiSportsResponse<Boolean>().success().data(courseStudentService.save(courseStudent));
        }
    }


    /**
     * 校验课程是否已经开始
     *
     * @param weeks        星期
     * @param startTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @return
     */
    private boolean isCheckStart(String weeks, String startTimeStr, String endTimeStr) {
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(startTimeStr);
        // 课程的结束时间
        LocalTime endTime = LocalTime.parse(endTimeStr);
        // 当前时间
        LocalTime currentTime = LocalTime.now();
        // 获取今天是星期几
        int week = LocalDateTime.now().getDayOfWeek().getValue() + 1;
        // 判断今天是否课程执行的星期
        boolean isCheckTime = weeks.contains(String.valueOf(week));
        // 今天是否是执行日 startTime > currentTime && currentTime > endTime = true
        return isCheckTime && startTime.isBefore(currentTime) && currentTime.isBefore(endTime);
    }

    @Override
    @TeacherRole
    @Log("查询课程的完成情况历史统计分析")
    public AiSportsResponse<IPage<CourseRecordVo>> findHistoryAnalysis(@RequestBody @Valid CourseQuery query) throws AiSportsException {
        return new AiSportsResponse<IPage<CourseRecordVo>>().success().data(courseRecordService.findByCourseId(query.getRequest(), query.getCourseId()));
    }

    @Override
    @Log("查询上课的历史记录")
    public AiSportsResponse<IPage<RecordStudentVo>> findCourseHistory(@RequestBody @Valid UserCourseQuery query) {
        Long userId = getCurrentUserId();
        if (Objects.nonNull(query.getUserId())) {
            User user = userService.getById(query.getUserId());
            if (user == null) {
                return new AiSportsResponse<IPage<RecordStudentVo>>().fail().message("用户Id不存在！");
            }

            if (!Objects.equals(RoleEnum.STUDENT.value(), user.getRoleId())) {
                return new AiSportsResponse<IPage<RecordStudentVo>>().fail().message("用户Id必须是学生！");
            }
        }
        return new AiSportsResponse<IPage<RecordStudentVo>>().success().data(recordStudentService.findRecordStudentVo(query.getRequest(), userId));
    }

    @Override
    @Log("查询我报名的正在进行的课程")
    public AiSportsResponse<List<CourseVo>> findMyEntryByCurrent() {
        return new AiSportsResponse<List<CourseVo>>().success().data(courseService.findMyEntryByCurrent(getCurrentUserId()));
    }
}
