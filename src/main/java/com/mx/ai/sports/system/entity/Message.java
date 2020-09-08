package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息表
 *
 * @author Mengjiaxin
 * @date 2020/9/8 5:49 下午
 */
@Data
@TableName("SYS_MESSAGE")
public class Message implements Serializable {

    private static final long serialVersionUID = -2632818002736052029L;

    /**
     * 系统消息Id
     */
    @TableId(value = "MESSAGE_ID", type = IdType.AUTO)
    private Long messageId;

    /**
     * 标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 消息类型 1课程发布通知 2课程开始前的通知
     */
    @TableField("TYPE")
    private String type;

    /**
     * 状态 1已读 2未读
     */
    @TableField("STATUS")
    private String status;

    /**
     * 课程Id
     */
    @TableField(value = "COURSE_ID")
    private Long courseId;


}
