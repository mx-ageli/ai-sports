package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生课程完成情况信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class StudentCourseVo implements Serializable {


    private static final long serialVersionUID = -5758357160233271443L;

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("性别 1男 2女")
    private String sex;

    @ApiModelProperty("头像，oss地址")
    private String avatar;

    @ApiModelProperty("姓名")
    private String fullName;

    @ApiModelProperty("是否缺席")
    private Boolean isAbsent;

    @ApiModelProperty("是否合格")
    private Boolean isPass;

    @ApiModelProperty("是否迟到")
    private Boolean isLate;


}
