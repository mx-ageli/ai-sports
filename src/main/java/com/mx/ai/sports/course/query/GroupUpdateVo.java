package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改小组
 * @author Mengjiaxin
 * @date 2020/9/28 3:45 下午
 */
@Data
public class GroupUpdateVo implements Serializable {

    private static final long serialVersionUID = 315119736703562498L;

    @NotNull(message = "小组Id不能为null")
    @ApiModelProperty("小组Id")
    private Long groupId;

    @NotNull(message = "小组名称不能为null")
    @ApiModelProperty("小组名称")
    private String groupName;


}
