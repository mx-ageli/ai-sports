package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 跑步规则信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunRuleVo implements Serializable {

    private static final long serialVersionUID = 6236530944740290234L;

    @ApiModelProperty("课程Id")
    private Long runRuleId;

    @ApiModelProperty("跑步规则类型： 1大于等于多少公里，2大于等于的时长")
    private String ruleType;

    @ApiModelProperty("里程")
    private Float mileage;

    @ApiModelProperty("跑步时长(以秒为单位)")
    private Long runTime;
}
