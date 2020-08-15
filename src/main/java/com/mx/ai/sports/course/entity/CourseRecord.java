package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程记录表，执行一次课程这个表就添加一条记录 每天根据课程批量生产数据
 *
 * @author Mengjiaxin
 * @date 2020/8/7 4:39 下午
 */
@Data
@TableName("APP_COURSE_RECORD")
public class CourseRecord implements Serializable {

    private static final long serialVersionUID = 3787483565510954926L;

    /**
     * 课程记录Id
     */
    @TableId(value = "COURSE_RECORD_ID", type = IdType.AUTO)
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
     * 当前这次参加的总人数
     */
    @TableField("ALL_COUNT")
    private Long allCount;

    /**
     * 打卡人数
     */
    @TableField("SINGED_COUNT")
    private Long singedCount;

    /**
     * 缺席人数
     */
    @TableField("ABSENT_COUNT")
    private Long absentCount;

    /**
     * 迟到人数
     */
    @TableField("LATE_COUNT")
    private Long lateCount;

    /**
     * 合格人数
     */
    @TableField("PASS_COUNT")
    private Long passCount;

    /**
     * 不合格人数
     */
    @TableField("NO_PASS_COUNT")
    private Long noPassCount;

}
