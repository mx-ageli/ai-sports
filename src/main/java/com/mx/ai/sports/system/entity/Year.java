package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学年表
 * @author Mengjiaxin
 * @date 2020/8/4 11:24 上午
 */
@Data
@TableName("SYS_YEAR")
public class Year implements Serializable {

    private static final long serialVersionUID = -4645905087895867724L;

    /**
     * 学年Id
     */
    @TableId(value = "YEAR_ID", type = IdType.AUTO)
    private Long yearId;

    /**
     * 学年名称
     */
    @TableField("YEAR_NAME")
    private String yearName;

    /**
     * 年份
     */
    @TableField("YEAR")
    private Integer year;

}
