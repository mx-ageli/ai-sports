package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级信息表
 * @author Mengjiaxin
 * @date 2019-08-20 19:48
 */
@Data
@TableName("SYS_CLASSES")
public class Classes implements Serializable {


    private static final long serialVersionUID = 1834103382381420831L;
    /**
     * 班级Id
     */
    @TableId(value = "CLASSES_ID", type = IdType.AUTO)
    private Long classesId;

    /**
     * 班级名称
     */
    @TableField("CLASSES_NAME")
    private String classesName;

    /**
     * 学校Id
     */
    @TableId(value = "SCHOOL_ID")
    private Long schoolId;

    /**
     * 头像，oss地址
     */
    @TableField("AVATAR")
    private String avatar;

}
