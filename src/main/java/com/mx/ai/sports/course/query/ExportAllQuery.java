package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 导出条件
 * @author Mengjiaxin
 * @date 2020/9/18 4:48 下午
 */
@Data
public class ExportAllQuery implements Serializable {

    private static final long serialVersionUID = 8104214418911896732L;

    @NotNull(message = "开始时间不能为null")
    @ApiModelProperty("时间范围 开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为null")
    @ApiModelProperty("时间范围 结束时间")
    private Date endTime;

}
