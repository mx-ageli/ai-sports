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
 * 跑步表
 * @author Mengjiaxin
 * @date 2020/8/11 5:17 下午
 */
@Data
@TableName("APP_RUN")
public class Run implements Serializable {

    private static final long serialVersionUID = 3067235858367555006L;

    /**
     * 跑步Id
     */
    @TableId(value = "RUN_ID", type = IdType.AUTO)
    private Long runId;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 用户Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 跑步状态： 1合格 2不合格
     */
    @TableField("STATUS")
    private String status;

    /**
     * 跑步结束的坐标点
     */
    @TableField("LOCATION")
    private String location;

    /**
     * 跑步结束的坐标点的具体名称
     */
    @TableField("LOCATION_NAME")
    private String locationName;

    /**
     * 跑步时长(以秒为单位)
     */
    @TableField("RUN_TIME")
    private Long runTime;

    /**
     * 里程
     */
    @TableField("MILEAGE")
    private Float mileage;

    /**
     * 配速 km/h, 分钟/km
     *
     */
    @TableField("SPEED")
    private Float speed;



}
