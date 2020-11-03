package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.CourseApi;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.*;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.DateUtil;
import com.mx.ai.sports.common.utils.SpringContextUtil;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseRecord;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.entity.Group;
import com.mx.ai.sports.course.query.*;
import com.mx.ai.sports.course.service.*;
import com.mx.ai.sports.course.vo.*;
import com.mx.ai.sports.job.entity.Job;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.UserSimple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    @Autowired
    private IGroupService groupService;

    @Override
    @TeacherRole
    @Log("查询我发布的课程列表")
    public AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest request) throws AiSportsException {
        return new AiSportsResponse<IPage<CourseVo>>().success().data(courseService.findAll(request, getCurrentUserId()));
    }

    @Override
    @TeacherRole
    @Log("老师创建课程")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid CourseAddVo addVo) throws AiSportsException {
        // 校验课程名称是否重复
        checkCourseName(null, addVo.getCourseName());
        // 校验课程时间数据的范围校验
        checkUpdateCourseTime(addVo.getStartTime(), addVo.getEndTime(), addVo.getSignedTime());
        // 校验小组数量和课程上限数量
        if (addVo.getGroupCount() == null || addVo.getGroupCount() <= 0) {
            return new AiSportsResponse<Boolean>().fail().message("小组数量必须大于0");
        }
        if (addVo.getMaxCount() == null || addVo.getMaxCount() <= 0) {
            return new AiSportsResponse<Boolean>().fail().message("课程上限人数必须大于0");
        }
        if (addVo.getMaxCount() < addVo.getGroupCount()) {
            return new AiSportsResponse<Boolean>().fail().message("课程上限人数必须大于小组数量");
        }

        // 老师新增课程，并创建定时任务
        courseService.saveCourse(addVo, getCurrentUserId());

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    /**
     * 校验小组数量和课程上限数量
     *
     * @param groupCount
     * @param maxCount
     * @throws AiSportsException
     */
    private void checkGroupCount(Integer groupCount, Integer maxCount) throws AiSportsException {

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
     * @param startTimeStr  开始时间
     * @param endTimeStr    结束时间
     * @param signedTimeStr 签到时间
     * @throws AiSportsException
     */
    private void checkUpdateCourseTime(String startTimeStr, String endTimeStr, String signedTimeStr) throws AiSportsException {
        // 还需要校验 开始时间结束时间的范围差值
        // 开始时间
        LocalTime startTime = LocalTime.parse(startTimeStr);
        // 结束时间
        LocalTime endTime = LocalTime.parse(endTimeStr);
        // 打卡时间
        LocalTime signedTime = LocalTime.parse(signedTimeStr);
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
        Course course = courseService.getCacheById(updateVo.getCourseId());
        if (course == null) {
            return new AiSportsResponse<Boolean>().fail().message("课程Id不存在，没有查询到数据!");
        }

        // 校验课程名称是否重复, 排除当前课程
        checkCourseName(updateVo.getCourseId(), updateVo.getCourseName());
        // 校验课程时间数据的范围校验
        checkUpdateCourseTime(updateVo.getStartTime(), updateVo.getEndTime(), updateVo.getSignedTime());
        // 校验当前时间节点是否有已经开始的课程在进行，如果有不能进行修改
        if (isCheckStart(course.getWeek(), course.getStartTime(), course.getEndTime())) {
            return new AiSportsResponse<Boolean>().fail().message("当前课程正在进行中，不能修改! 请等本次课程结束后再修改！");
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

        // 判断当前用户是否已经报名这个课程
        if (courseIds.contains(courseVo.getCourseId())) {
            courseVo.setEntryStatus(EntryEnum.ENTRY.value());
        }

        // 如果课程已经开始了，设置状态为不可报名
        if (isCheckStart(courseVo.getWeek(), courseVo.getStartTime(), courseVo.getEndTime())) {
            courseVo.setEntryStatus(EntryEnum.NO.value());
        }
        int week = DateUtil.getWeek();
        // 判断今天是否课程执行的星期
        boolean isCheckTime = courseVo.getWeek().contains(String.valueOf(week));
        // 如果今日不是课程日
        if (!isCheckTime) {
            courseVo.setEntryStatus(EntryEnum.NO.value());
        }

        if (isCheckTime && LocalTime.parse(courseVo.getEndTime()).isBefore(LocalTime.now())) {
            courseVo.setEntryStatus(EntryEnum.FINISH.value());
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

        // 查询当前这个课程我所在的小组
        Group group = groupService.findMyGroup(user.getUserId(), courseId);
        if (group != null) {
            courseVo.setGroupId(group.getGroupId());
            courseVo.setGroupName(group.getGroupName());
        }

        // 赋值课程状态
        setEntryStatus(courseIds, courseVo);
        return new AiSportsResponse<CourseVo>().success().data(courseVo);
    }

    @Override
    @Log("学生报名课程")
    @Limit(key = "entry", period = 3, count = 1, name = "报课", prefix = "limit", limitType = LimitType.IP)
    public AiSportsResponse<CourseEntryVo> entry(@NotNull @RequestParam("courseId") Long courseId) throws AiSportsException {

        Course course = courseService.getCacheById(courseId);
        if (course == null) {
            return new AiSportsResponse<CourseEntryVo>().fail().message("课程Id不存在，没有查询到数据!");
        }
        Long userId = getCurrentUserId();
        // 当前时间
        LocalTime currentTime = LocalTime.now();
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 课程的结束时间
        LocalTime endTime = LocalTime.parse(course.getEndTime());
        // 课程是否已经开始
        // 今天是否是执行日 startTime > currentTime && currentTime > endTime = true
        boolean isCheckStart = startTime.isBefore(currentTime) && currentTime.isBefore(endTime);

        // 查询学生是否已经报课
        String isEntryStudent = courseStudentService.findEntryStudentList2Redis(courseId, userId);
        // 如果不存在说明学生还没有报课，只有在学生还没有报课才走下面的校验逻辑。
        if(StringUtils.isBlank(isEntryStudent)){
            // 先判断课程是否是今天的课程
            // 获取今天是星期几
            int week = DateUtil.getWeek();
            // 判断今天是否课程执行的星期
            boolean isCheckTime = course.getWeek().contains(String.valueOf(week));
            // 如果今日不是课程日
            if (!isCheckTime) {
                return new AiSportsResponse<CourseEntryVo>().fail().message("今天不是课程日，不能预约！");
            }
            if (isCheckStart) {
                return new AiSportsResponse<CourseEntryVo>().fail().message("当前报名课程已经开始，不能报名！");
            }
            // 只有在正式环境才使用这个规则
            if (ActiveProfileConstant.PROD.equals(SpringContextUtil.getActiveProfile())) {
                // 预约时间提示
                String tip = "请在" + AiSportsConstant.ENTRY_START_TIME + "-" + AiSportsConstant.ENTRY_END_TIME + "内进行课程预约！";
                // 判断当前时间是否在课程的预约时间范围内
                if (LocalTime.parse(AiSportsConstant.ENTRY_START_TIME).isAfter(currentTime)) {
                    return new AiSportsResponse<CourseEntryVo>().fail().message("还没有到课程的预约时间，" + tip);
                }
                if (LocalTime.parse(AiSportsConstant.ENTRY_END_TIME).isBefore(currentTime)) {
                    return new AiSportsResponse<CourseEntryVo>().fail().message("已经错过了课程预约时间，" + tip);
                }
            }
            // 先查询课程在redis中的报名人数是否已满
            Long entryCountRedis = courseStudentService.getLenEntryStudentList2Redis(courseId);
            // 如果报名的人数大于了课程的最大人数
            if(entryCountRedis != null && entryCountRedis >= course.getMaxCount()){
                return new AiSportsResponse<CourseEntryVo>().fail().message("今日当前课程已经报满，请明日再来！");
            }
            // 默认先给学生报名成功
            Long currentStudentSize = courseStudentService.setEntryStudentList2Redis(courseId, userId);
            // 查询数据库中当前报名课程的人数
            Long entryCount = courseStudentService.findCountByUserId(courseId);
            // 如果报名的人数大于了课程的最大人数
            if(entryCount >= course.getMaxCount()){
                // 在不满足报名条件时，删除报名记录
                courseStudentService.removeEntryStudentList2Redis(courseId, userId);
                return new AiSportsResponse<CourseEntryVo>().fail().message("今日当前课程已经报满，请明日再来！");
            }
            // 判断学生有没有报其他的课程，如果学生报了其他的课程，就不能再报
            List<CourseStudent> courseStudentList = courseStudentService.findByUserNoCourseId(userId, courseId);
            if (CollectionUtils.isNotEmpty(courseStudentList)) {
                // 在不满足报名条件时，删除报名记录
                courseStudentService.removeEntryStudentList2Redis(courseId, userId);
                return new AiSportsResponse<CourseEntryVo>().fail().message("今日你已经报了其他课程，不能再报该课程了！");
            }

            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setCourseId(courseId);
            courseStudent.setUserId(userId);

            CourseEntryVo courseEntryVo = courseStudentService.saveStudentAndGroup(courseStudent);
            courseEntryVo.setSort(currentStudentSize);

            return new AiSportsResponse<CourseEntryVo>().success().data(courseEntryVo);

        } else {
            if (isCheckStart) {
                return new AiSportsResponse<CourseEntryVo>().fail().message("当前报名课程已经开始，不能取消报名！");
            }
            // TODO 临时代码，在报名时间开始的半个小时内不能从这里取消课程
            LocalTime localTime = LocalTime.parse(AiSportsConstant.ENTRY_START_TIME);
            localTime = localTime.plusHours(1);
            if (localTime.isAfter(currentTime)) {
                return new AiSportsResponse<CourseEntryVo>().success().data(new CourseEntryVo());
            }

            // 删除报名信息
            courseStudentService.remove(userId, courseId);
            return new AiSportsResponse<CourseEntryVo>().success().data(new CourseEntryVo());
        }
    }

    @Override
    @Log("学生取消报名")
    public AiSportsResponse<CourseEntryVo> cancelEntry(@NotNull @RequestParam("courseId") Long courseId) throws AiSportsException {

        Course course = courseService.getCacheById(courseId);
        if (course == null) {
            return new AiSportsResponse<CourseEntryVo>().fail().message("课程Id不存在，没有查询到数据!");
        }
        Long userId = getCurrentUserId();
        // 当前时间
        LocalTime currentTime = LocalTime.now();
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 课程的结束时间
        LocalTime endTime = LocalTime.parse(course.getEndTime());
        // 课程是否已经开始
        // 今天是否是执行日 startTime > currentTime && currentTime > endTime = true
        boolean isCheckStart = startTime.isBefore(currentTime) && currentTime.isBefore(endTime);

        if (isCheckStart) {
            return new AiSportsResponse<CourseEntryVo>().fail().message("当前报名课程已经开始，不能取消报名！");
        }
        // 删除报名信息
        courseStudentService.remove(userId, courseId);
        return new AiSportsResponse<CourseEntryVo>().success().data(new CourseEntryVo());
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
        int week = DateUtil.getWeek();
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

            userId = query.getUserId();
        }
        return new AiSportsResponse<IPage<RecordStudentVo>>().success().data(recordStudentService.findRecordStudentVo(query.getRequest(), userId));
    }

    @Override
    @Log("查询我报名的正在进行的课程")
    public AiSportsResponse<List<CourseVo>> findMyEntryByCurrent() {
        return new AiSportsResponse<List<CourseVo>>().success().data(courseService.findMyEntryByCurrent(getCurrentUserId()));
    }
}
