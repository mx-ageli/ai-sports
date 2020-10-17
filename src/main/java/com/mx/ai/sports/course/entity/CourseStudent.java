package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程的报名学生表
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
@Data
@TableName("APP_COURSE_STUDENT")
public class CourseStudent implements Serializable {

    private static final long serialVersionUID = -7912383942877987508L;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 学生Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 报名顺序
     */
    @TableId(value = "SORT")
    private Long sort;


}
