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
//        List<User> teachers = userService.findByRole(RoleEnum.TEACHER.value());
//        // 老师的姓名对应的老师Id
//        Map<String, Long> teacherMap = teachers.stream().collect(Collectors.toMap(User::getFullName, User::getUserId));
//
//        log.info("查询到老师数量，{}个", teacherMap.size());
//
//        Map<String, CourseAddVo> CourseAddVoMap = initCourseData();
//
//        log.info("初始化课程数量，{}个", CourseAddVoMap.size());
//        CourseAddVoMap.forEach((name, addVo) -> {
//
//            // 老师新增课程，并创建定时任务
//            try {
//                log.info("开始添加{}的课程，详情：{}", name, JSON.toJSONString(addVo));
//                courseService.saveCourse(addVo, teacherMap.get(name));
//            } catch (AiSportsException e) {
//                e.printStackTrace();
//            }
//        });


    }

    private Map<String, CourseAddVo> initCourseData() {
        Map<String, CourseAddVo> courseAddVoMap = new HashMap<>(10);

        CourseAddVo courseAddVo9 = defaultCourseAddVo();
        courseAddVo9.setCourseName("健身跑");
        courseAddVo9.setWeek("5");
        courseAddVo9.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/jianshenpao.png");
        courseAddVo9.setContent("健身跑");
        courseAddVo9.setIsRun(true);
        courseAddVo9.setMaxCount(1500);
        courseAddVo9.setGroupCount(30);
        courseAddVoMap.put("辛坤宗", courseAddVo9);

        CourseAddVo courseAddVo6 = defaultCourseAddVo();
        courseAddVo6.setCourseName("峨眉武术");
        courseAddVo6.setWeek("4");
        courseAddVo6.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiwushu.png");
        courseAddVo6.setContent("峨眉武术");
        courseAddVo6.setIsRun(false);
        courseAddVo6.setMaxCount(500);
        courseAddVo6.setGroupCount(10);
        courseAddVoMap.put("王克超", courseAddVo6);

        CourseAddVo courseAddVo7 = defaultCourseAddVo();
        courseAddVo7.setCourseName("峨眉养生功");
        courseAddVo7.setWeek("3");
        courseAddVo7.setImages("https://kaoba880.oss-cn-beijing.aliyuncs.com/uploads/course/emeiyangshenggong.png");
        courseAddVo7.setContent("峨眉养生功");
        courseAddVo7.setIsRun(false);
        courseAddVo7.setMaxCount(500);
        courseAddVo7.setGroupCount(10);
        courseAddVoMap.put("卢新伟", courseAddVo7);


        return courseAddVoMap;
    }

    private CourseAddVo defaultCourseAddVo() {
        CourseAddVo courseAddVo = new CourseAddVo();
        courseAddVo.setStartTime("18:15");
        courseAddVo.setEndTime("18:45");
        courseAddVo.setSignedTime("17:45");
        courseAddVo.setLat("29.58926113");
        courseAddVo.setLon("105.0481993");
        courseAddVo.setLocationName("田径场");
        courseAddVo.setScope(100L);
        courseAddVo.setStatus("1");

        return courseAddVo;
    }
}
