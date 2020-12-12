package com.mx.ai.sports.system.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 时间段查询
 * @author Mengjiaxin
 * @date 2020/12/11 下午2:22
 */
@Data
public class UserCountQuery implements Serializable {

    private static final long serialVersionUID = 7303265597921828321L;

    @NotNull(message = "开始时间不能为null")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为null")
    @ApiModelProperty("结束时间")
    private Date endTime;


}
