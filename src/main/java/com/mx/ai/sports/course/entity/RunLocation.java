package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 跑步坐标表
 *
 * @author Mengjiaxin
 * @date 2020/8/11 5:17 下午
 */
@Data
@TableName("APP_RUN_LOCATION")
public class RunLocation implements Serializable {

    private static final long serialVersionUID = 7769607729263950753L;

    /**
     * 跑步Id
     */
    @TableId(value = "RUN_ID")
    private Long runId;

    /**
     * 坐标json
     */
    @TableId(value = "LOCATION")
    private String location;


}
