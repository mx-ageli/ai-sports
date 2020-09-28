package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Mengjiaxin
 * @date 2020/9/25 3:21 下午
 */
@Data
public class GroupQuery implements Serializable {

    private static final long serialVersionUID = 368647401151119156L;

    /**
     * 小组Id
     */
    @NotNull(message = "小组Id不能为null")
    @ApiModelProperty("小组Id")
    private Long groupId;

    /**
     * 分页参数
     */
    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
