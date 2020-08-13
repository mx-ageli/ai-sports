package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户简单信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class UserSmallVo implements Serializable {

    private static final long serialVersionUID = -4454594099007335062L;

    /**
     * 用户Id
     */
    @ApiModelProperty("用户Id")
    private Long userId;

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
     * 学号
     */
    @ApiModelProperty("学号")
    private String sno;
}
