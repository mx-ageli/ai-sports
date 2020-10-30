package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 健身查询条件
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class KeepRecordQuery implements Serializable {

    private static final long serialVersionUID = 5169267991164206322L;

    @ApiModelProperty("健身状态，不传查询全部，1查询合格，2查询不合格")
    private String status;

    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
