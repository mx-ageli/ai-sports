package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报名后返回
 * @author Mengjiaxin
 * @date 2020/10/17 11:10 下午
 */
@Data
public class CourseEntryVo implements Serializable {

    private static final long serialVersionUID = -9194573906734304472L;

    @ApiModelProperty("报名序号")
    private Long sort;

    @ApiModelProperty("小组Id")
    private Long groupId;

    @ApiModelProperty("小组名称")
    private String groupName;
}
