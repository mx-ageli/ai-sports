package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 主课程表
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:13 下午
 */
@Data
@TableName("APP_SUBJECT")
public class Subject implements Serializable {

    private static final long serialVersionUID = 5516746294631823695L;

    /**
     * 主课Id
     */
    @TableId(value = "SUBJECT_ID", type = IdType.AUTO)
    private Long subjectId;

    /**
     * 主课名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 编号
     */
    @TableField("NUMBER")
    private String number;

    public Subject() {
    }

    public Subject(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
