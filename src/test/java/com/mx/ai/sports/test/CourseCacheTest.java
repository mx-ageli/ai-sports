package com.mx.ai.sports.test;

import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.course.query.CourseAddVo;
import com.mx.ai.sports.course.service.ICourseService;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseCacheTest {

    @Autowired
    private ICourseStudentService courseStudentService;

    @Autowired
    private ICourseService courseService;

    /**
     * 初始化课程数据
     */
    @Test
    public void cache() {
        courseStudentService.setEntryStudentList2Redis(7L, 10L);
    }
}
