package com.mx.ai.sports.system.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询班级学生列表参数
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class ClassesQuery implements Serializable {


    private static final long serialVersionUID = -7164471467593679607L;

    /**
     * 班级Id
     */
    @ApiModelProperty("班级Id")
    private Long classesId;

    /**
     * 分页参数
     */
    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
