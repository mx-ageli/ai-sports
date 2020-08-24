package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * 新增跑步记录
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunAddVo implements Serializable {

    private static final long serialVersionUID = 2423181785969332187L;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @NotNull(message = "开始时间不能为null")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为null")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @NotNull(message = "跑步结束的坐标点不能为null")
    @ApiModelProperty("跑步结束的坐标点")
    private String location;

    @NotNull(message = "跑步结束的坐标点的具体名称不能为null")
    @ApiModelProperty("跑步结束的坐标点的具体名称")
    private String locationName;

    @NotNull(message = "跑步时长不能为null")
    @ApiModelProperty("跑步时长(以秒为单位)")
    private Long runTime;

    @NotNull(message = "里程不能为null")
    @ApiModelProperty("里程")
    private Float mileage;

    @NotNull(message = "配速不能为null")
    @ApiModelProperty("配速 km/h, 分钟/km")
    private Float speed;
}
