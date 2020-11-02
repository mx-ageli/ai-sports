package com.mx.ai.sports.course.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数量统计
 * @author Mengjiaxin
 * @date 2020/11/2 7:50 下午
 */
@Data
public class CountVo implements Serializable {

    private static final long serialVersionUID = -6014259687090604318L;

    @ApiModelProperty("全部数量")
    private String key;

    @ApiModelProperty("数量")
    private Long count;

}
