package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 小组内学生vo
 *
 * @author Mengjiaxin
 * @date 2020/9/25 2:36 下午
 */
@Data
public class GroupStudentVo implements Serializable {

    private static final long serialVersionUID = 7380903472209118140L;

    @ApiModelProperty("学生Id")
    private Long userId;

    @ApiModelProperty("性别 1男 2女")
    private String sex;

    @ApiModelProperty("头像，oss地址")
    private String avatar;

    @ApiModelProperty("学生姓名")
    private String fullName;

}
