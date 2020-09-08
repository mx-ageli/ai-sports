package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户对应的系统消息表
 *
 * @author Mengjiaxin
 * @date 2020/9/8 5:49 下午
 */
@Data
@TableName("SYS_USER_MESSAGE")
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 909501331941746594L;

    /**
     * 系统消息Id
     */
    @TableId(value = "MESSAGE_ID")
    private Long messageId;

    /**
     * 用户Id
     */
    @TableField("USER_ID")
    private Long userId;


}
