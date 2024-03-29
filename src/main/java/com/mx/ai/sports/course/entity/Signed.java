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
 * 打卡表
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
@Data
@TableName("APP_SIGNED")
public class Signed implements Serializable {

    private static final long serialVersionUID = -2476380036181859769L;

    /**
     * 打卡Id
     */
    @TableId(value = "SIGNED_ID", type = IdType.AUTO)
    private Long signedId;

    /**
     * 课程记录Id
     */
    @TableId(value = "COURSE_RECORD_ID")
    private Long courseRecordId;

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
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 上课打卡时间
     */
    @TableId(value = "START_TIME")
    private Date startTime;

    /**
     * 下课打卡时间
     */
    @TableId(value = "END_TIME")
    private Date endTime;

    /**
     * 是否迟到
     */
    @TableField("IS_LATE")
    private Boolean isLate;

    /**
     * 上课打卡的坐标点 纬度
     */
    @TableId(value = "START_LAT")
    private String startLat;

    /**
     * 上课打卡的坐标点 经度
     */
    @TableId(value = "START_LON")
    private String startLon;

    /**
     * 上课坐标点的名称
     */
    @TableId(value = "START_LOCATION_NAME")
    private String startLocationName;


    /**
     * 下课打卡的坐标点 纬度
     */
    @TableId(value = "END_LAT")
    private String endLat;

    /**
     * 下课打卡的坐标点 经度
     */
    @TableId(value = "END_LON")
    private String endLon;

    /**
     * 下课坐标点的名称
     */
    @TableId(value = "END_LOCATION_NAME")
    private String endLocationName;

}
