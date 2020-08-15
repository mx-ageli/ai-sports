package com.mx.ai.sports.system.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 班级信息修改
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class ClassesUpdateVo implements Serializable {

    private static final long serialVersionUID = -7420156191175904467L;

    @ApiModelProperty("班级Id")
    private Long classesId;

    @NotNull(message = "班级名称不能为null")
    @ApiModelProperty("班级名称")
    private String classesName;

    @NotNull(message = "班主任的用户Id不能为null")
    @ApiModelProperty("班主任的用户Id")
    private Long userId;

    @NotNull(message = "头像不能为null")
    @ApiModelProperty("头像，oss地址")
    private String avatar;

}
