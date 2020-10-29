package com.mx.ai.sports.test;

import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.job.task.CourseTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteCourseTest {

    @Autowired
    private CourseTask courseTask;

    /**
     * 初始化课程数据
     */
    @Test
    public void delete() {
        courseTask.deleteCourseStudentTask("4");
    }


}
