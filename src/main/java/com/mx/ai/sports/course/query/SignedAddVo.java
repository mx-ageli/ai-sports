package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 打卡参数
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class SignedAddVo implements Serializable {

    private static final long serialVersionUID = -8591600180382873362L;

    @NotNull
    @ApiModelProperty("课程Id")
    private Long courseId;

    @NotNull(message = "纬度不能为null")
    @ApiModelProperty("打卡的坐标点 纬度")
    private String lat;

    @NotNull(message = "经度不能为null")
    @ApiModelProperty("打卡的坐标点 经度")
    private String lon;

    @NotNull(message = "打卡的坐标点的具体名称不能为null")
    @ApiModelProperty("打卡的坐标点的具体名称")
    private String locationName;
}
