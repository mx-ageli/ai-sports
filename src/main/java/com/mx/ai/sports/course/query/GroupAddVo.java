package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加小组
 * @author Mengjiaxin
 * @date 2020/9/28 3:45 下午
 */
@Data
public class GroupAddVo implements Serializable {

    private static final long serialVersionUID = 315119736703562498L;

    @NotNull(message = "课程Id不能为null")
    @ApiModelProperty("课程Id")
    private Long courseId;

    @NotNull(message = "小组名称不能为null")
    @ApiModelProperty("小组名称")
    private String groupName;

    @ApiModelProperty("小组上限人数，预留字段，不传后台会取其他小组的上限人数来使用")
    private Integer maxCount;

}
