package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新小组内的学生
 *
 * @author Mengjiaxin
 * @date 2020/9/25 4:44 下午
 */
@Data
public class GroupStudentUpdateVo implements Serializable {

    private static final long serialVersionUID = 315119736703562498L;

    @NotNull(message = "学生当前所在的小组Id不能为null")
    @ApiModelProperty("学生当前所在的小组Id")
    private Long currentGroupId;

    @NotNull(message = "更换的小组Id不能为null")
    @ApiModelProperty("更换的小组Id")
    private Long groupId;

    @NotNull(message = "学生Id不能为null")
    @ApiModelProperty("学生Id")
    private Long userId;

}
