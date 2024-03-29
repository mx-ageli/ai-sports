package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生每次课程的记录表, 每天根据课程批量生产数据
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
@Data
@TableName("APP_RECORD_STUDENT")
public class RecordStudent implements Serializable {

    private static final long serialVersionUID = 3787483565510954926L;

    /**
     * 学生的课程记录Id
     */
    @TableId(value = "RECORD_STUDENT_ID", type = IdType.AUTO)
    private Long recordStudentId;

    /**
     * 课程记录Id
     */
    @TableId(value = "COURSE_RECORD_ID")
    private Long courseRecordId;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 学生Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 是否缺席
     */
    @TableField("IS_ABSENT")
    private Boolean isAbsent;

    /**
     * 是否迟到
     */
    @TableField("IS_LATE")
    private Boolean isLate;

    /**
     * 是否合格
     */
    @TableField("IS_PASS")
    private Boolean isPass;

    /**
     * 是否早退
     */
    @TableField("IS_GONE")
    private Boolean isGone;


    public RecordStudent(Long courseId, Long courseRecordId, Long studentId){
        this.setCourseId(courseId);
        this.setCourseRecordId(courseRecordId);
        this.setUserId(studentId);
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        // 默认缺席
        this.setIsAbsent(Boolean.TRUE);
        // 默认迟到
        this.setIsLate(Boolean.FALSE);
        // 默认为不合格
        this.setIsPass(Boolean.FALSE);
        // 默认早退
        this.setIsGone(Boolean.TRUE);
    }

}
