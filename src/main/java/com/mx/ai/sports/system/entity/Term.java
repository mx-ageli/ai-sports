package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学期表
 * @author Mengjiaxin
 * @date 2019-08-20 19:48
 */
@Data
@TableName("SYS_TERM")
public class Term implements Serializable {


    private static final long serialVersionUID = 2608009305574207132L;

    /**
     * 学期Id
     */
    @TableId(value = "TERM_ID", type = IdType.AUTO)
    private Long termId;

    /**
     * 学期名称
     */
    @TableField("TERM_NAME")
    private String termName;

    /**
     * 开始时间
     */
    @TableField("BEGIN_TIME")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 学期类型（1上学期 2下学期）
     */
    @TableField("TYPE")
    private String type;

    /**
     * 学年Id
     */
    @TableId("YEAR_ID")
    private Long yearId;

}
