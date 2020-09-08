package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息列表
 * @author Mengjiaxin
 * @date 2020/9/8 6:13 下午
 */
@Data
public class MessageVo implements Serializable {

    private static final long serialVersionUID = 714949041202172579L;

    @ApiModelProperty("学校Id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("消息Id")
    private Long messageId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private Date content;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("消息类型 1课程发布通知 2课程开始前的通知")
    private String type;

    @ApiModelProperty("状态 1已读 2未读")
    private String status;

    @ApiModelProperty("课程Id")
    private Long courseId;
}
