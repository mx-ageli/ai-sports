package com.mx.ai.sports.app.controller;

import com.mx.ai.sports.app.api.SignedApi;
import com.mx.ai.sports.common.annotation.Limit;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.LimitType;
import com.mx.ai.sports.common.utils.DateUtil;
import com.mx.ai.sports.course.converter.SignedConverter;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.entity.Signed;
import com.mx.ai.sports.course.query.SignedAddVo;
import com.mx.ai.sports.course.service.ICourseRecordService;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.service.ISignedService;
import com.mx.ai.sports.course.vo.SignedVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;


/**
 * 打卡签到相关接口
 *
 * @author Mengjiaxin
 * @date 2019-08-28 16:04
 */
@Slf4j
@RestController("SignedApi")
public class SignedController extends BaseRestController implements SignedApi {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICourseRecordService courseRecordService;

    @Autowired
    private ISignedService signedService;

    @Autowired
    private ICourseStudentService courseStudentService;

    @Autowired
    private SignedConverter signedConverter;

    @Override
//    @Log("通过课程Id查询我最近的一次打卡记录")
    public AiSportsResponse<SignedVo> findLastByCourseId(@NotNull @RequestParam("courseId") Long courseId) {
        Signed signed = signedService.findLastByCourseId(courseId, getCurrentUserId());
        if (signed == null) {
            return new AiSportsResponse<SignedVo>().success().message("没到查询到打卡记录！");
        }
        return new AiSportsResponse<SignedVo>().success().data(signedConverter.domain2Vo(signed));
    }

    @Override
//    @Log("通过课程记录Id查询我以往的课程打卡记录")
    public AiSportsResponse<SignedVo> findByCourseRecordId(@NotNull @RequestParam("courseRecordId") Long courseRecordId) {
        Signed signed = signedService.findByCourseRecordId(courseRecordId, getCurrentUserId());
        if (signed == null) {
            return new AiSportsResponse<SignedVo>().success().message("没到查询到打卡记录！");
        }
        return new AiSportsResponse<SignedVo>().success().data(signedConverter.domain2Vo(signed));
    }

    @Override
    @Log("打卡")
    @Limit(key = "signed", period = 3, count = 1, name = "打卡", prefix = "limit", limitType = LimitType.IP, message = "正在提交打卡，请稍后再试...")
    public AiSportsResponse<Boolean> signed(@RequestBody @Valid SignedAddVo signedAddVo) {
        Course course = courseService.getById(signedAddVo.getCourseId());

        // 当前学生Id
        Long userId = getCurrentUserId();
        // 查询学生是否报名该课程
        CourseStudent courseStudent = courseStudentService.findByUserCourseId(userId, signedAddVo.getCourseId());
        if (courseStudent == null) {
            return new AiSportsResponse<Boolean>().fail().message("你没有报名这个课程，不能打卡！");
        }

        // 获取今天是星期几
        int week = DateUtil.getWeek();
        if (!course.getWeek().contains(String.valueOf(week))) {
            return new AiSportsResponse<Boolean>().fail().message("还没有到打卡时间，不能打卡！");
        }

        // 课程的打卡时间
        LocalTime signedTime = LocalTime.parse(course.getSignedTime());
        // 课程的开始时间
        LocalTime startTime = LocalTime.parse(course.getStartTime());
        // 课程的结束时间
        LocalTime endTime = LocalTime.parse(course.getEndTime());
        // 当前时间
        LocalTime currentTime = LocalTime.now();

        // 当前时间 < 打卡时间 不能打卡
        if (currentTime.isBefore(signedTime)) {
            return new AiSportsResponse<Boolean>().fail().message("还没有到打卡时间，不能打卡！请在" + signedTime + "后打卡！");
        }

        // 查询到当前课程记录Id
        Long courseRecordId = courseRecordService.findIdByNowAndCreate(signedAddVo.getCourseId());
        // 学生的打卡记录
        Signed signed = signedService.findByCourseRecordId(courseRecordId, userId);
        // 是否是第一次打卡
        boolean isFirst = false;
        // 说明学生还没有打卡过，这个时候打的都是上课卡
        if (signed == null) {

            // 当前时间 > 课程结束时间 在没有一次打卡记录的时候，为缺席，不能打卡
            if (currentTime.isAfter(endTime)) {
                return new AiSportsResponse<Boolean>().fail().message("课程已经结束，你已缺席，不能打卡！");
            }
            // 正常打上课卡
            signed = new Signed();
            signed.setCourseId(signedAddVo.getCourseId());
            signed.setCourseRecordId(courseRecordId);
            signed.setUserId(userId);
            signed.setCreateTime(new Date());
            signed.setStartTime(new Date());
            signed.setIsLate(false);
            signed.setStartLat(signedAddVo.getLat());
            signed.setStartLon(signedAddVo.getLon());
            signed.setStartLocationName(signedAddVo.getLocationName());

            // 当前时间 > 课程的开始时间 打迟到卡
            if (currentTime.isAfter(startTime)) {
                signed.setIsLate(true);
                isFirst = true;
            }
        } else { // 存在记录了说明已经打过上课卡了，或者是重复的打下课卡

            if (signed.getEndTime() != null) {
                return new AiSportsResponse<Boolean>().fail().message("已经打卡完成，无需重复打卡！");
            }
            // 当前时间 < 上课时间 不能打卡
            if (currentTime.isBefore(startTime)) {
                return new AiSportsResponse<Boolean>().fail().message("课程还没有开始，不能打下课卡！");
            }

            LocalTime endTimePlus30 = endTime.plusMinutes(30);
            // 当前时间 < 下课时间 不能打卡
            if (currentTime.isBefore(endTime)) {
                return new AiSportsResponse<Boolean>().fail().message("现在还没有下课，不能打下课卡！请在" + endTime + "-" + endTimePlus30 + "之间打下课卡！");
            }
            // 当前时间 > 下课时间+30分钟 也就是19:15分钟后 不能打卡
            if (currentTime.isAfter(endTimePlus30)) {
                return new AiSportsResponse<Boolean>().fail().message("你已经错过了打卡时间，视为早退！应在" + endTime + "-" + endTimePlus30 + "之间打下课卡！");
            }


            // 正常打下课卡
            signed.setEndTime(new Date());
            signed.setEndLat(signedAddVo.getLat());
            signed.setEndLon(signedAddVo.getLon());
            signed.setEndLocationName(signedAddVo.getLocationName());

        }
        // 保存打卡记录
        AiSportsResponse<Boolean> response = new AiSportsResponse<Boolean>().success().data(signedService.saveSigned(signed));
        return signed.getIsLate() && isFirst ? response.message("迟到打卡！请下次在" + signedTime + "-" + startTime + "之间打卡！") : response.message("打卡成功！");
    }
}
