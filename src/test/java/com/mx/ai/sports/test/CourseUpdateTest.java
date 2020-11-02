package com.mx.ai.sports.test;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.query.CourseAddVo;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.query.CourseUpdateVo;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.IGroupService;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseUpdateTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IGroupService groupService;

    /**
     * 初始化课程数据
     */
    @Test
    public void initCourse() {
        // 查询出所有的老师数据
        List<User> teachers = userService.findByRole(RoleEnum.TEACHER.value());
        // 老师的姓名对应的老师Id
        Map<String, Long> teacherMap = teachers.stream().collect(Collectors.toMap(User::getFullName, User::getUserId));

        log.info("查询到老师数量，{}个", teacherMap.size());

//        Map<String, CourseUpdateVo> courseUpdateVoMap = initCourseData();
        Map<String, CourseUpdateVo> courseUpdateVoMap = new HashMap<>();

        log.info("初始化课程数量，{}个", courseUpdateVoMap.size());
        courseUpdateVoMap.forEach((name, updateVo) -> {

            // 老师新增课程，并创建定时任务
            try {
                log.info("开始添加{}的课程，详情：{}", name, JSON.toJSONString(updateVo));

                Course course = courseService.getCacheById(updateVo.getCourseId());
                if (course == null) {
                    throw new AiSportsException("课程Id不存在，没有查询到数据!");
                }

                Long userId = teacherMap.get(name);

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
                course.setGroupCount(updateVo.getGroupCount());
                course.setMaxCount(updateVo.getMaxCount());
                if (StringUtils.isNotBlank(updateVo.getStatus())) {
                    course.setStatus(updateVo.getStatus());
                }
                // 更新课程信息
                courseService.updateCourse(course, userId);
                // 更新小组信息
                groupService.batchCreate(course, updateVo.getGroupCount(), updateVo.getMaxCount());

            } catch (AiSportsException e) {
                e.printStackTrace();
            }
        });


    }

    private Map<String, CourseUpdateVo> initCourseData() {
        Map<String, CourseUpdateVo> courseUpdateVoMap = new HashMap<>(10);

        CourseUpdateVo courseUpdateVo1 = defaultCourseUpdateVo();
        courseUpdateVo1.setCourseId(10L);
        courseUpdateVo1.setCourseName("群众健身舞（操)");
        courseUpdateVo1.setWeek("2");
        courseUpdateVo1.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/%E7%BE%A4%E4%BC%97%E5%81%A5%E8%BA%AB%E8%88%9E.png");
        courseUpdateVo1.setContent("群众健身舞（操)");
        courseUpdateVo1.setIsRun(false);
        courseUpdateVo1.setMaxCount(2000);
        courseUpdateVo1.setGroupCount(40);
        courseUpdateVoMap.put("叶莲子", courseUpdateVo1);

        CourseUpdateVo courseUpdateVo2 = defaultCourseUpdateVo();
        courseUpdateVo2.setCourseId(7L);
        courseUpdateVo2.setCourseName("健身走");
        courseUpdateVo2.setWeek("2,3");
        courseUpdateVo2.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenzou.png");
        courseUpdateVo2.setContent("健身走");
        courseUpdateVo2.setIsRun(true);
        courseUpdateVo2.setMaxCount(1500);
        courseUpdateVo2.setGroupCount(30);
        courseUpdateVoMap.put("邵乙珩", courseUpdateVo2);

        CourseUpdateVo courseUpdateVo3 = defaultCourseUpdateVo();
        courseUpdateVo3.setCourseId(1L);
        courseUpdateVo3.setCourseName("峨眉搏击");
        courseUpdateVo3.setWeek("2");
        courseUpdateVo3.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiboji.png");
        courseUpdateVo3.setContent("峨眉搏击");
        courseUpdateVo3.setIsRun(false);
        courseUpdateVo3.setMaxCount(500);
        courseUpdateVo3.setGroupCount(10);
        courseUpdateVoMap.put("吴保占", courseUpdateVo3);

        CourseUpdateVo courseUpdateVo4 = defaultCourseUpdateVo();
        courseUpdateVo4.setCourseId(8L);
        courseUpdateVo4.setCourseName("跆拳道");
        courseUpdateVo4.setWeek("2,5");
        courseUpdateVo4.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/taiquandao.png");
        courseUpdateVo4.setContent("跆拳道");
        courseUpdateVo4.setIsRun(false);
        courseUpdateVo4.setMaxCount(500);
        courseUpdateVo4.setGroupCount(10);
        courseUpdateVoMap.put("卢月", courseUpdateVo4);

        CourseUpdateVo courseUpdateVo5 = defaultCourseUpdateVo();
        courseUpdateVo5.setCourseId(5L);
        courseUpdateVo5.setCourseName("峨眉女子防身术");
        courseUpdateVo5.setWeek("3,4");
        courseUpdateVo5.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeinvzifangshenshu.png");
        courseUpdateVo5.setContent("峨眉女子防身术");
        courseUpdateVo5.setIsRun(false);
        courseUpdateVo5.setMaxCount(700);
        courseUpdateVo5.setGroupCount(14);
        courseUpdateVoMap.put("付成林", courseUpdateVo5);

        CourseUpdateVo courseUpdateVo6 = defaultCourseUpdateVo();
        courseUpdateVo6.setCourseId(6L);
        courseUpdateVo6.setCourseName("峨眉武术");
        courseUpdateVo6.setWeek("3");
        courseUpdateVo6.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiwushu.png");
        courseUpdateVo6.setContent("峨眉武术");
        courseUpdateVo6.setIsRun(false);
        courseUpdateVo6.setMaxCount(2000);
        courseUpdateVo6.setGroupCount(40);
        courseUpdateVoMap.put("许艳玲", courseUpdateVo6);

        CourseUpdateVo courseUpdateVo7 = defaultCourseUpdateVo();
        courseUpdateVo7.setCourseId(3L);
        courseUpdateVo7.setCourseName("峨眉养生功");
        courseUpdateVo7.setWeek("5");
        courseUpdateVo7.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiyangshenggong.png");
        courseUpdateVo7.setContent("峨眉养生功");
        courseUpdateVo7.setIsRun(false);
        courseUpdateVo7.setMaxCount(500);
        courseUpdateVo7.setGroupCount(10);
        courseUpdateVoMap.put("邢程", courseUpdateVo7);

        CourseUpdateVo courseUpdateVo8 = defaultCourseUpdateVo();
        courseUpdateVo8.setCourseId(2L);
        courseUpdateVo8.setCourseName("健美操");
        courseUpdateVo8.setWeek("4");
        courseUpdateVo8.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianmeicao.png");
        courseUpdateVo8.setContent("健美操");
        courseUpdateVo8.setIsRun(false);
        courseUpdateVo8.setMaxCount(2000);
        courseUpdateVo8.setGroupCount(40);
        courseUpdateVoMap.put("张凤娟", courseUpdateVo8);

        CourseUpdateVo courseUpdateVo9 = defaultCourseUpdateVo();
        courseUpdateVo9.setCourseId(4L);
        courseUpdateVo9.setCourseName("健身跑");
        courseUpdateVo9.setWeek("4");
        courseUpdateVo9.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenpao.png");
        courseUpdateVo9.setContent("健身跑");
        courseUpdateVo9.setIsRun(true);
        courseUpdateVo9.setMaxCount(1500);
        courseUpdateVo9.setGroupCount(30);
        courseUpdateVoMap.put("郭恩次", courseUpdateVo9);

        CourseUpdateVo courseUpdateVo10 = defaultCourseUpdateVo();
        courseUpdateVo10.setCourseId(9L);
        courseUpdateVo10.setCourseName("搏击操");
        courseUpdateVo10.setWeek("5");
        courseUpdateVo10.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/bojicao%402x.png");
        courseUpdateVo10.setContent("搏击操");
        courseUpdateVo10.setIsRun(false);
        courseUpdateVo10.setMaxCount(2000);
        courseUpdateVo10.setGroupCount(40);
        courseUpdateVoMap.put("马传奇", courseUpdateVo10);

        CourseUpdateVo courseUpdateVo11 = defaultCourseUpdateVo();
        courseUpdateVo11.setCourseId(14L);
        courseUpdateVo11.setCourseName("健身跑");
        courseUpdateVo11.setWeek("5");
        courseUpdateVo11.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenpao.png");
        courseUpdateVo11.setContent("健身跑");
        courseUpdateVo11.setIsRun(true);
        courseUpdateVo11.setMaxCount(1500);
        courseUpdateVo11.setGroupCount(30);
        courseUpdateVoMap.put("辛坤宗", courseUpdateVo11);

        CourseUpdateVo courseUpdateVo12 = defaultCourseUpdateVo();
        courseUpdateVo12.setCourseId(15L);
        courseUpdateVo12.setCourseName("峨眉武术");
        courseUpdateVo12.setWeek("4");
        courseUpdateVo12.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiwushu.png");
        courseUpdateVo12.setContent("峨眉武术");
        courseUpdateVo12.setIsRun(false);
        courseUpdateVo12.setMaxCount(500);
        courseUpdateVo12.setGroupCount(10);
        courseUpdateVoMap.put("王克超", courseUpdateVo12);

        CourseUpdateVo courseUpdateVo13 = defaultCourseUpdateVo();
        courseUpdateVo13.setCourseId(13L);
        courseUpdateVo13.setCourseName("峨眉养生功");
        courseUpdateVo13.setWeek("3");
        courseUpdateVo13.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiyangshenggong.png");
        courseUpdateVo13.setContent("峨眉养生功");
        courseUpdateVo13.setIsRun(false);
        courseUpdateVo13.setMaxCount(500);
        courseUpdateVo13.setGroupCount(10);
        courseUpdateVoMap.put("卢新伟", courseUpdateVo13);

        return courseUpdateVoMap;
    }

    private CourseUpdateVo defaultCourseUpdateVo() {
        CourseUpdateVo courseUpdateVo = new CourseUpdateVo();
        courseUpdateVo.setStartTime("18:15");
        courseUpdateVo.setEndTime("18:45");
        courseUpdateVo.setSignedTime("17:45");
        courseUpdateVo.setLat("29.58926113");
        courseUpdateVo.setLon("105.0481993");
        courseUpdateVo.setLocationName("田径场");
        courseUpdateVo.setScope(100L);
        courseUpdateVo.setStatus("1");

        return courseUpdateVo;
    }
}
