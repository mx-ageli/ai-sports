package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 主课序号表
 * @author Mengjiaxin
 * @date 2020/10/14 5:15 下午
 */
@Data
@TableName("APP_SUBJECT_SEQ")
public class SubjectSeq implements Serializable {

    private static final long serialVersionUID = -8595064378236347102L;

    /**
     * 主课序号Id
     */
    @TableId(value = "SUBJECT_SEQ_ID", type = IdType.AUTO)
    private Long subjectSeqId;

    /**
     * 主课Id
     */
    @TableId(value = "SUBJECT_ID")
    private Long subjectId;

    /**
     * 序号
     */
    @TableField("SEQ")
    private String seq;


}
