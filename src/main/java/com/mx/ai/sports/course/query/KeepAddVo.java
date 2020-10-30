package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增健身记录
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class KeepAddVo implements Serializable {

    private static final long serialVersionUID = 8378893289426571312L;

    @NotNull(message = "课程Id不能为null")
    @ApiModelProperty("课程Id")
    private Long courseId;

    @NotNull(message = "开始时间不能为null")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为null")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @NotNull(message = "健身时长不能为null")
    @ApiModelProperty("健身时长(以秒为单位)")
    private Long keepTime;

}
