package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mx.ai.sports.common.annotation.IsTime;
import lombok.Data;

import java.io.Serializable;
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
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 发布的老师Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 星期，分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 ... 周天7 格式： 以逗号拼接 1,2,3,4,5,6,7
     */
    @TableId(value = "WEEK")
    private String week;

    /**
     * 课程的开始时间
     */
    @IsTime(message = "时间格式不正确, 格式：HH:mm")
    @TableId(value = "START_TIME")
    private String startTime;

    /**
     * 课程的结束时间
     */
    @IsTime(message = "时间格式不正确, 格式：HH:mm")
    @TableId(value = "END_TIME")
    private String endTime;

    /**
     * 课程打卡的时间
     */
    @IsTime(message = "时间格式不正确, 格式：HH:mm")
    @TableId(value = "SIGNED_TIME")
    private String signedTime;

    /**
     * 打卡的坐标点 纬度
     */
    @TableId(value = "LAT")
    private String lat;

    /**
     * 打卡的坐标点 经度
     */
    @TableId(value = "LON")
    private String lon;

    /**
     * 坐标点的别名
     */
    @TableId(value = "LOCATION_NAME")
    private String locationName;

    /**
     * 以坐标点打卡的范围，以米为单位
     */
    @TableId(value = "SCOPE")
    private Long scope;

    /**
     * 课程对应的图片
     */
    @TableId(value = "IMAGES")
    private String images;

    /**
     * 课程记录的任务Id
     */
    @TableId(value = "COURSE_JOB_ID")
    private Long courseJobId;

    /**
     * 课程学生记录的任务Id
     */
    @TableId(value = "STUDENT_JOB_ID")
    private Long studentJobId;

    /**
     * 课程开始前的推送的任务Id
     */
    @TableId(value = "START_JOB_ID")
    private Long startJobId;

    /**
     * 状态 1=正常,2=暂停
     */
    @TableField("STATUS")
    private String status;

    /**
     * 课程简介
     */
    @TableField("CONTENT")
    private String content;


}
