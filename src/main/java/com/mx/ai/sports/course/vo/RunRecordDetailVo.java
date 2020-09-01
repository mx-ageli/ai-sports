package com.mx.ai.sports.course.vo;


import com.mx.ai.sports.course.query.RunLocationAddVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    @ApiModelProperty("坐标")
    private List<RunLocationAddVo> location;

    @ApiModelProperty("跑步时长(以秒为单位)")
    private Long runTime;

    @ApiModelProperty("里程")
    private Float mileage;


}
