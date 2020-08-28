package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学校列表
 * @author Mengjiaxin
 * @date 2020/8/28 9:54 上午
 */
@Data
public class SchoolVo implements Serializable {

    private static final long serialVersionUID = 3027470772470140419L;

    @ApiModelProperty("学校Id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

}
