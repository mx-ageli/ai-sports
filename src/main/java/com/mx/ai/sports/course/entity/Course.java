package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * 课程表
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
@Data
@TableName("APP_COURSE")
public class Course implements Serializable {

    private static final long serialVersionUID = -2476380036181859769L;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID", type = IdType.AUTO)
    private Long courseId;

    /**
     * 课程名称
     */
    @TableField("COURSE_NAME")
    private String courseName;

    /**
     * 课程编号
     */
    @TableField("COURSE_NO")
    private String courseNo;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 发布的老师Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 星期，分别用 1周一 2周二 ... 周天7 格式： 1,2,3,4
     */
    @TableId(value = "WEEK")
    private String week;

    /**
     * 课程的开始时间
     */
    @TableId(value = "START_TIME")
    private Time startTime;

    /**
     * 课程的结束时间
     */
    @TableId(value = "END_TIME")
    private Time endTime;

    /**
     * 课程打卡的开始时间
     */
    @TableId(value = "SIGNED_START_TIME")
    private Time signedStartTime;

    /**
     * 课程打卡的结束时间
     */
    @TableId(value = "SIGNED_END_TIME")
    private Time signedEndTime;

    /**
     * 打卡的坐标点
     */
    @TableId(value = "LOCATION")
    private String location;

    /**
     * 坐标点的别名
     */
    @TableId(value = "LOCATION_NAME")
    private String locationName;

    /**
     * 以坐标点打卡的范围，以米为单位
     */
    @TableId(value = "RANGE")
    private Long range;

    /**
     * 课程对应的图片
     */
    @TableId(value = "IMAGES")
    private String images;



}
