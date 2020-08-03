package com.mx.ai.sports.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询参数
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:17
 */
@Data
@ToString
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;
    /**
     * 当前页面数据量
     */
    @ApiModelProperty(value = "当前页面数据量", example = "10")
    private int pageSize = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", example = "1")
    private int pageNum = 1;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String field;
    /**
     *
     */
    @ApiModelProperty(value = "排序规则，asc升序，desc降序")
    private String order;
}
