package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.CourseApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.converter.CourseConverter;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.query.CourseQuery;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.query.StudentCourseQuery;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.vo.CourseNumVo;
import com.mx.ai.sports.course.vo.CourseVo;
import com.mx.ai.sports.course.vo.StudentCourseVo;
import com.mx.ai.sports.job.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;


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
    private CourseConverter courseConverter;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IJobService jobService;

    @Override
    public AiSportsResponse<IPage<CourseVo>> findMyPublish(@RequestBody @Valid QueryRequest query) {
        return null;
    }

    @Override
    @TeacherRole
    @Log("老师创建课程")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid CourseUpdateVo updateVo) throws AiSportsException {
        // 校验课程名称是否重复
        checkCourseName(null, updateVo.getCourseName());
        // 校验课程时间数据的范围校验
        checkUpdateCourseTime(updateVo);

        Course course = courseConverter.vo2Domain(updateVo);
        course.setUserId(getCurrentUserId());
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());

        // 先保存课程数据
        courseService.save(course);
        // 创建课程相关的定时任务
        course.setCourseJobId(jobService.createCourseRecordJob(course));
        // 创建同步学生报名列表的任务
        course.setStudentJobId(jobService.createRecordStudentJob(course));
        // 更新课程任务
        return new AiSportsResponse<Boolean>().success().data(courseService.saveOrUpdate(course));
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
        // 开始时间必须大于结束时间
        // startTime < endTime = true
        if (startTime.isAfter(endTime)) {
            throw new AiSportsException("开始时间必须大于结束时间!");
        }
        // 可打卡时间必须大于开始时间
        // signedTime < startTime = true
        if (signedTime.isAfter(startTime)) {
            throw new AiSportsException("可打卡时间必须大于开始时间!");
        }
    }

    /**
     * 校验当前的课程时间数据的范围校验
     *
     * @throws AiSportsException
     */
    private void checkCourseTime(Course course) throws AiSportsException {
        // 开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 结束时间
        LocalTime endTime = LocalTime.parse(course.getEndTime());
        // 当前时间
        LocalTime currentTime = LocalTime.now();
        // 开始时间大于当前时间，当前时间小于当前时间，说明当前时间在两个时间范围内
        // startTime > currentTime && currentTime > endTime = true
        if (startTime.isBefore(currentTime) && currentTime.isBefore(endTime)) {
            throw new AiSportsException("当前课程正在进行中，不能修改! 请等本次课程结束后再修改！");
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
        checkCourseTime(course);

        course.setUserId(getCurrentUserId());
        course.setCourseName(updateVo.getCourseName());
        course.setUpdateTime(new Date());
        course.setWeek(updateVo.getWeek());
        course.setStartTime(updateVo.getStartTime());
        course.setEndTime(updateVo.getEndTime());
        course.setSignedTime(updateVo.getSignedTime());
        course.setLocation(updateVo.getLocation());
        course.setLocationName(updateVo.getLocationName());
        course.setScope(updateVo.getScope());
        course.setImages(updateVo.getImages());

        // 先把以前的课程任务删除
        jobService.deleteJobs(new String[]{course.getCourseJobId().toString(), course.getStudentJobId().toString()});
        // 创建课程相关的定时任务
        course.setCourseJobId(jobService.createCourseRecordJob(course));
        // 创建同步学生报名列表的任务
        course.setStudentJobId(jobService.createRecordStudentJob(course));
        // 更新课程信息
        return new AiSportsResponse<Boolean>().success().data(courseService.saveOrUpdate(course));
    }

    @Override
    public AiSportsResponse<IPage<Object>> findHistoryAnalysis(@RequestBody @Valid CourseQuery query) {
        return null;
    }

    @Override
    public AiSportsResponse<CourseNumVo> findNumById(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<StudentCourseVo>> findStudentById(@RequestBody @Valid StudentCourseQuery query) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<CourseVo>> findAll(@RequestBody @Valid QueryRequest query) {
        return null;
    }

    @Override
    public AiSportsResponse<CourseVo> findById(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<Boolean> entry(@NotNull @RequestParam("courseId") Long courseId) {
        return null;
    }

    @Override
    public AiSportsResponse<IPage<Object>> findCourseHistory(@RequestBody @Valid QueryRequest query) {
        return null;
    }
}
