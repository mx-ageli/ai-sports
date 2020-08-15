package com.mx.ai.sports.system.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息修改
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class UserUpdateVo implements Serializable {

    private static final long serialVersionUID = -6663000439115926466L;

    /**
     * 性别 1男 2女
     */
    @ApiModelProperty("性别 1男 2女")
    private String sex;

    /**
     * 班级Id
     */
    @ApiModelProperty("班级Id")
    private Long classesId;

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
