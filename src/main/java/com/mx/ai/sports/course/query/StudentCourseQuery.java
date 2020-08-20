package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.entity.QueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 课程完成情况查询条件
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class StudentCourseQuery implements Serializable {

    private static final long serialVersionUID = -6289608891436053475L;

    @NotNull(message = "课程记录Id不能为null")
    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @NotNull(message = "课程完成类型不能为null")
    @ApiModelProperty("课程完成类型：1全部 2缺席 3合格 4不合格")
    private String type;

    @NotNull(message = "分页参数不能为null")
    @ApiModelProperty(value = "分页参数")
    private QueryRequest request;
}
