package com.mx.ai.sports.test;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.common.entity.RoleEnum;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.query.CourseAddVo;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICourseService courseService;

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

        Map<String, CourseAddVo> CourseAddVoMap = initCourseData();

        log.info("初始化课程数量，{}个", CourseAddVoMap.size());
        CourseAddVoMap.forEach((name, addVo) -> {

            // 老师新增课程，并创建定时任务
            try {
                log.info("开始添加{}的课程，详情：{}", name, JSON.toJSONString(addVo));
                courseService.saveCourse(addVo, teacherMap.get(name));
            } catch (AiSportsException e) {
                e.printStackTrace();
            }
        });


    }

    private Map<String, CourseAddVo> initCourseData() {
        Map<String, CourseAddVo> courseAddVoMap = new HashMap<>(10);

        CourseAddVo courseAddVo1 = defaultCourseAddVo();
        courseAddVo1.setCourseName("群众健身舞（操)");
        courseAddVo1.setWeek("2");
        courseAddVo1.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/%E7%BE%A4%E4%BC%97%E5%81%A5%E8%BA%AB%E8%88%9E.png");
        courseAddVo1.setContent("群众健身舞（操)");
        courseAddVo1.setIsRun(false);
        courseAddVo1.setMaxCount(1000);
        courseAddVo1.setGroupCount(20);
        courseAddVoMap.put("叶莲子", courseAddVo1);

        CourseAddVo courseAddVo2 = defaultCourseAddVo();
        courseAddVo2.setCourseName("健身走");
        courseAddVo2.setWeek("2,3");
        courseAddVo2.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenzou.png");
        courseAddVo2.setContent("健身走");
        courseAddVo2.setIsRun(true);
        courseAddVo2.setMaxCount(2000);
        courseAddVo2.setGroupCount(40);
        courseAddVoMap.put("邵乙珩", courseAddVo2);

        CourseAddVo courseAddVo3 = defaultCourseAddVo();
        courseAddVo3.setCourseName("峨眉搏击");
        courseAddVo3.setWeek("2");
        courseAddVo3.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiboji.png");
        courseAddVo3.setContent("峨眉搏击");
        courseAddVo3.setIsRun(false);
        courseAddVo3.setMaxCount(500);
        courseAddVo3.setGroupCount(10);
        courseAddVoMap.put("吴保占", courseAddVo3);

        CourseAddVo courseAddVo4 = defaultCourseAddVo();
        courseAddVo4.setCourseName("跆拳道");
        courseAddVo4.setWeek("2,4");
        courseAddVo4.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/taiquandao.png");
        courseAddVo4.setContent("跆拳道");
        courseAddVo4.setIsRun(false);
        courseAddVo4.setMaxCount(800);
        courseAddVo4.setGroupCount(16);
        courseAddVoMap.put("卢月", courseAddVo4);

        CourseAddVo courseAddVo5 = defaultCourseAddVo();
        courseAddVo5.setCourseName("峨眉女子防身术");
        courseAddVo5.setWeek("3,4");
        courseAddVo5.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeinvzifangshenshu.png");
        courseAddVo5.setContent("峨眉女子防身术");
        courseAddVo5.setIsRun(false);
        courseAddVo5.setMaxCount(700);
        courseAddVo5.setGroupCount(14);
        courseAddVoMap.put("付成林", courseAddVo5);

        CourseAddVo courseAddVo6 = defaultCourseAddVo();
        courseAddVo6.setCourseName("峨眉武术");
        courseAddVo6.setWeek("3,5");
        courseAddVo6.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiwushu.png");
        courseAddVo6.setContent("峨眉武术");
        courseAddVo6.setIsRun(false);
        courseAddVo6.setMaxCount(700);
        courseAddVo6.setGroupCount(14);
        courseAddVoMap.put("许艳玲", courseAddVo6);

        CourseAddVo courseAddVo7 = defaultCourseAddVo();
        courseAddVo7.setCourseName("峨眉养生功");
        courseAddVo7.setWeek("3,5");
        courseAddVo7.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiyangshenggong.png");
        courseAddVo7.setContent("峨眉养生功");
        courseAddVo7.setIsRun(false);
        courseAddVo7.setMaxCount(800);
        courseAddVo7.setGroupCount(16);
        courseAddVoMap.put("邢程", courseAddVo7);

        CourseAddVo courseAddVo8 = defaultCourseAddVo();
        courseAddVo8.setCourseName("健美操");
        courseAddVo8.setWeek("4");
        courseAddVo8.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianmeicao.png");
        courseAddVo8.setContent("健美操");
        courseAddVo8.setIsRun(false);
        courseAddVo8.setMaxCount(1000);
        courseAddVo8.setGroupCount(20);
        courseAddVoMap.put("张凤娟", courseAddVo8);

        CourseAddVo courseAddVo9 = defaultCourseAddVo();
        courseAddVo9.setCourseName("健身跑");
        courseAddVo9.setWeek("4,5");
        courseAddVo9.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenpao.png");
        courseAddVo9.setContent("健身跑");
        courseAddVo9.setIsRun(true);
        courseAddVo9.setMaxCount(1500);
        courseAddVo9.setGroupCount(30);
        courseAddVoMap.put("郭恩次", courseAddVo9);

        CourseAddVo courseAddVo10 = defaultCourseAddVo();
        courseAddVo10.setCourseName("搏击操");
        courseAddVo10.setWeek("5");
        courseAddVo10.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/bojicao%402x.png");
        courseAddVo10.setContent("搏击操");
        courseAddVo10.setIsRun(false);
        courseAddVo10.setMaxCount(1000);
        courseAddVo10.setGroupCount(20);
        courseAddVoMap.put("马传奇", courseAddVo10);

        return courseAddVoMap;
    }

    private CourseAddVo defaultCourseAddVo() {
        CourseAddVo courseAddVo = new CourseAddVo();
        courseAddVo.setStartTime("18:00");
        courseAddVo.setEndTime("18:50");
        courseAddVo.setSignedTime("17:45");
        courseAddVo.setLat("29.58926113");
        courseAddVo.setLon("105.0481993");
        courseAddVo.setLocationName("田径场");
        courseAddVo.setScope(100L);
        courseAddVo.setStatus("1");

        return courseAddVo;
    }
}
