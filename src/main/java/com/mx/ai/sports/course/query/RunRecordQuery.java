package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 课程查询条件
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunRecordQuery implements Serializable {

    private static final long serialVersionUID = 962330801132278243L;

    @NotNull(message = "开始时间不能为null")
    @ApiModelProperty("时间范围 开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为null")
    @ApiModelProperty("时间范围 结束时间")
    private Date endTime;

    @ApiModelProperty("是否合格，不传查询全部，1查询合格，2查询不合格")
    private String isPass;

    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
