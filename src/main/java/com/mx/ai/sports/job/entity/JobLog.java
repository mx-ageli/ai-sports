package com.mx.ai.sports.job.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 调度日志信息表
 *
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
@Data
@TableName("SYS_JOB_LOG")
@Excel("调度日志信息表")
public class JobLog implements Serializable {

    // 任务执行成功
    public static final String JOB_SUCCESS = "1";
    // 任务执行失败
    public static final String JOB_FAIL = "2";

    private static final long serialVersionUID = -6808706124458929857L;

    /**
     * 日志Id
     */
    @TableId(value = "LOG_ID", type = IdType.AUTO)
    private Long logId;

    /**
     * 对应的任务Id
     */
    @TableField("JOB_ID")
    private Long jobId;

    /**
     *  Bean名称
     */
    @TableField("BEAN_NAME")
    private String beanName;

    /**
     * 方法名称
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 方法参数
     */
    @TableField("PARAMS")
    private String params;

    /**
     * 状态 1=成功,2=失败
     */
    @TableField("STATUS")
    private String status;

    /**
     * 异常信息
     */
    @TableField("ERROR")
    private String error;

    /**
     * 耗时（毫秒）
     */
    @TableField("TIMES")
    private Long times;

    /**
     * 执行时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
