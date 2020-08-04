package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息表
 * @author Mengjiaxin
 * @date 2019-08-20 19:48
 */
@Data
@TableName("SYS_ROLE")
public class Role implements Serializable {

    private static final long serialVersionUID = -4493960686192269860L;

    /**
     * 角色ID
     */
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 角色描述
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
}
