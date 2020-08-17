package com.mx.ai.sports.job.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mx.ai.sports.common.annotation.IsCron;
import com.wuwenze.poi.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务信息表
 * @author Mengjiaxin
 * @date 2020/8/17 7:17 下午
 */
@Data
@TableName("SYS_JOB")
@Excel("定时任务信息表")
public class Job implements Serializable {

    private static final long serialVersionUID = 1505809579898100141L;

    /**
     * 任务调度参数 key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";


    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL("1"),
        /**
         * 暂停
         */
        PAUSE("2");

        private String value;

        ScheduleStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @TableId(value = "JOB_ID", type = IdType.AUTO)
    private Long jobId;

    /**
     * Bean名称
     */
    @TableField("bean_name")
    private String beanName;

    /**
     * 方法名称
     */
    @TableField("method_name")
    private String methodName;

    /**
     * 方法参数
     */
    @TableField("params")
    private String params;

    /**
     * Cron表达式
     */
    @TableField("cron_expression")
    @IsCron(message = "{invalid}")
    private String cronExpression;

    /**
     * 状态 1=正常,2=暂停
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
