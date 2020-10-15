package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生临时表
 * @author Mengjiaxin
 * @date 2020/10/15 3:18 下午
 */
@Data
@TableName("SYS_TEMP_STUDENT")
public class TempStudent implements Serializable {

    private static final long serialVersionUID = 5516746294631823695L;

    /**
     * 临时学生Id
     */
    @TableId(value = "TEMP_STUDENT_ID", type = IdType.AUTO)
    private Long tempStudentId;

    /**
     * 姓名
     */
    @TableField("FULL_NAME")
    private String fullName;

    /**
     * 学号
     */
    @TableField("SNO")
    private String sno;

    /**
     * 性别 1男 2女
     */
    @TableField("SEX")
    private String sex;

    /**
     * 学校Id
     */
    @TableField("SCHOOL_ID")
    private Long schoolId;

    /**
     * 班级Id
     */
    @TableField("CLASSES_ID")
    private Long classesId;

    /**
     * 主课Id
     */
    @TableField("SUBJECT_ID")
    private Long subjectId;

    /**
     * 主课序号Id
     */
    @TableField("SUBJECT_SEQ_ID")
    private Long subjectSeqId;

    /**
     * 主课老师Id
     */
    @TableField("TEACHER_ID")
    private Long teacherId;

    /**
     * 是否已经注册
     */
    @TableField("IS_REGISTER")
    private Boolean isRegister;
}
