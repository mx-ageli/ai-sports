package com.mx.ai.sports.test;

import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.course.service.ICourseStudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentUpdateTest {


    @Autowired
    private ICourseStudentService courseStudentService;


    /**
     * 初始化课程数据
     */
    @Test
    public void initCourse() {
        // 3 4 6 9
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(3L);
        courseIds.add(4L);
        courseIds.add(6L);
        courseIds.add(9L);

        for (Long courseId : courseIds) {
//            List<Long> userIds = courseStudentService.findByCourseId(courseId);
//
//            courseStudentService.removeCountByUserId2Redis(courseId);
//
//            courseStudentService.findCountByUserId2Redis(courseId);
//            for (Long userId : userIds) {
//                courseStudentService.setCountByUserId2Redis(courseId, 1L);
//                courseStudentService.setEntryStudentList2Redis(courseId, userId);
//            }
        }

    }


}
