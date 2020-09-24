package com.mx.ai.sports.course.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 学生课程累计打卡次数
 * @author Mengjiaxin
 * @date 2020/9/24 5:43 下午
 */
@Data
public class CourseSignedCountDto implements Serializable {

    private static final long serialVersionUID = 6373814529344476459L;

    private Long courseId;

    private Long userId;

    private Long signedId;

}
