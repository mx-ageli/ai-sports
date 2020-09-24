package com.mx.ai.sports.course.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 课程当前报名人数
 * @author Mengjiaxin
 * @date 2020/9/24 5:04 下午
 */
@Data
public class CourseStudentCountDto implements Serializable {

    private static final long serialVersionUID = 4377581914147323661L;

    private Long courseId;

    private Long currentCount;

}
