package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程序号与老师的关系表
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:15 下午
 */
@Data
@TableName("APP_SUBJECT_TEACHER")
public class SubjectTeacher implements Serializable {

    private static final long serialVersionUID = 7662141479431346892L;

    /**
     * 主课Id
     */
    @TableId(value = "SUBJECT_ID")
    private Long subjectId;

    /**
     * 主课序号Id
     */
    @TableId(value = "SUBJECT_SEQ_ID")
    private Long subjectSeqId;

    /**
     * 老师Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

}
