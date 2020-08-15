package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程数量信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class CourseNumVo implements Serializable {

    private static final long serialVersionUID = 2753426594273967451L;

    @ApiModelProperty("全部数量")
    private Long all;

    @ApiModelProperty("缺席数量")
    private Long absent;

    @ApiModelProperty("合格数量")
    private Long pass;

    @ApiModelProperty("不合格数量")
    private Long noPass;
}
