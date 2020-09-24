package com.mx.ai.sports.course.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 课程累计运动次数
 * @author Mengjiaxin
 * @date 2020/9/24 5:04 下午
 */
@Data
public class CourseRunCountDto implements Serializable {

    private static final long serialVersionUID = 3818848602087414694L;

    private Long courseId;

    private Long runCount;

}
