package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 小组vo
 *
 * @author Mengjiaxin
 * @date 2020/9/25 2:36 下午
 */
@Data
public class GroupVo implements Serializable {

    private static final long serialVersionUID = 7562157640855517613L;

    @ApiModelProperty("小组Id")
    private Long groupId;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("老师Id")
    private Long userId;

    @ApiModelProperty("小组名称")
    private String groupName;

    @ApiModelProperty("小组上限人数")
    private Integer maxCount;

    @ApiModelProperty("当前组内人数")
    private Integer currentCount;

}
