package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = -3964377585818464531L;

    /**
     * 用户Id
     */
    @ApiModelProperty("用户Id")
    private Long userId;

    /**
     * 用户名，电话号码
     */
    @ApiModelProperty("用户名，电话号码")
    private String username;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date modifyTime;

    /**
     * 最近一次登录时间
     */
    @ApiModelProperty("最近一次登录时间")
    private Date lastLoginTime;

    /**
     * 性别 1男 2女
     */
    @ApiModelProperty("性别 1男 2女")
    private String sex;

    /**
     * 头像，oss地址
     */
    @ApiModelProperty("头像，oss地址")
    private String avatar;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String fullName;

    /**
     * 设备Id
     */
    @ApiModelProperty("设备Id")
    private String deviceId;

    /**
     * 角色Id
     */
    @ApiModelProperty("角色Id")
    private Long roleId;

    /**
     * 学校Id
     */
    @ApiModelProperty("学校Id")
    private Long schoolId;

    /**
     * 学校名称
     */
    @ApiModelProperty("学校名称")
    private String schoolName;

    /**
     * 班级Id
     */
    @ApiModelProperty("班级Id")
    private Long classesId;

    /**
     * 班级名称
     */
    @ApiModelProperty("班级名称")
    private String classesName;

    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String sno;
}
