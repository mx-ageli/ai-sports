package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户课程查询条件
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class UserCourseQuery implements Serializable {

    private static final long serialVersionUID = 3031768919501965353L;

    /**
     * 用户Id
     */
    @ApiModelProperty("用户Id，为空就查询当前用户")
    private Long userId;

    /**
     * 分页参数
     */
    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
