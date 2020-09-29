package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 小组对应的学生
 * @author Mengjiaxin
 * @date 2020/9/25 11:18 上午
 */
@Data
@TableName("APP_GROUP_STUDENT")
public class GroupStudent implements Serializable {

    private static final long serialVersionUID = -5129530619182067892L;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 小组Id
     */
    @TableId(value = "GROUP_ID")
    private Long groupId;

    /**
     * 学生Id
     */
    @TableId(value = "USER_ID")
    private Long userId;


    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

}
