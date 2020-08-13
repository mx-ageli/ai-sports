package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 跑步规则表
 * @author Mengjiaxin
 * @date 2020/8/11 5:17 下午
 */
@Data
@TableName("APP_RUN_RULE")
public class RunRule implements Serializable {

    private static final long serialVersionUID = 1321791905062607766L;

    /**
     * 跑步规则Id
     */
    @TableId(value = "RUN_RULE_ID", type = IdType.AUTO)
    private Long runRuleId;

    /**
     * 跑步规则类型： 1大于等于多少公里，2大于等于的时长
     */
    @TableId(value = "RULE_TYPE")
    private String ruleType;

    /**
     * 里程
     */
    @TableField("MILEAGE")
    private float mileage;

    /**
     * 跑步时长(以秒为单位)
     */
    @TableField("RUN_TIME")
    private Long runTime;


}
