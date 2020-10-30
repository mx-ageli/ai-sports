package com.mx.ai.sports.course.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 跑步坐标表
 *
 * @author Mengjiaxin
 * @date 2020/8/11 5:17 下午
 */
@Data
@TableName("APP_RUN_POSITION")
public class RunPosition implements Serializable {

    private static final long serialVersionUID = 7769607729263950753L;

    /**
     * 跑步坐标Id
     */
    @JSONField(serialize = false)
    @TableId(value = "RUN_LOCATION_ID", type = IdType.AUTO)
    private Long runLocationId;

    /**
     * 跑步Id
     */
    @JSONField(serialize = false)
    @TableId(value = "RUN_ID")
    private Long runId;

    /**
     * 跑步结束的坐标点 纬度
     */
    @TableId(value = "LAT")
    private String lat;

    /**
     * 跑步结束的坐标点 经度
     */
    @TableId(value = "LON")
    private String lon;

    /**
     * 时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("TIME")
    private Date time;


}
