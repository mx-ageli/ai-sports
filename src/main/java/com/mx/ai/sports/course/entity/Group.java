package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程小组
 *
 * @author Mengjiaxin
 * @date 2020/9/25 11:33 上午
 */
@Data
@TableName("APP_GROUP")
public class Group implements Serializable {

    private static final long serialVersionUID = -9019340749120626431L;

    /**
     * 小组Id
     */
    @TableId(value = "GROUP_ID", type = IdType.AUTO)
    private Long groupId;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 老师Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 小组名称
     */
    @TableId(value = "GROUP_NAME")
    private String groupName;

    /**
     * 小组上限人数
     */
    @TableId(value = "MAX_COUNT")
    private Integer maxCount;

    /**
     * 当前组内人数
     */
    @TableId(value = "CURRENT_COUNT")
    private Integer currentCount;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

}