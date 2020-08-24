package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * 跑步记录信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunRecordDetailVo implements Serializable {


    private static final long serialVersionUID = -2360462021926074006L;

    @ApiModelProperty("是否合格")
    private Boolean isPass;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("跑步结束的坐标点")
    private String location;

    @ApiModelProperty("跑步结束的坐标点的具体名称")
    private String locationName;

    @ApiModelProperty("跑步时长(以秒为单位)")
    private Long runTime;

    @ApiModelProperty("里程")
    private Float mileage;


}
