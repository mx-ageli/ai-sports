package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课程查询条件
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class CourseQuery implements Serializable {

    private static final long serialVersionUID = 962330801132278243L;

    /**
     * 课程Id
     */
    @NotNull(message = "课程Id不能为null")
    @ApiModelProperty("课程Id")
    private Long courseId;

    /**
     * 分页参数
     */
    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
