package com.mx.ai.sports.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 健身表
 * @author Mengjiaxin
 * @date 2020/8/11 5:17 下午
 */
@Data
@TableName("APP_KEEP")
public class Keep implements Serializable {

    private static final long serialVersionUID = 765884581209030722L;

    /**
     * 健身Id
     */
    @TableId(value = "KEEP_ID", type = IdType.AUTO)
    private Long keepId;

    /**
     * 课程Id
     */
    @TableId(value = "COURSE_ID")
    private Long courseId;

    /**
     * 课程记录Id
     */
    @TableId(value = "COURSE_RECORD_ID")
    private Long courseRecordId;

    /**
     * 用户Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 健身状态： 1合格 2不合格
     */
    @TableField("STATUS")
    private String status;

    /**
     * 健身时长(以秒为单位)
     */
    @TableField("KEEP_TIME")
    private Long keepTime;

}
