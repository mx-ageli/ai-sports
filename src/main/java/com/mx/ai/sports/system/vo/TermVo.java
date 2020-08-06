package com.mx.ai.sports.system.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学期信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class TermVo implements Serializable {

    private static final long serialVersionUID = -5811547693255114651L;

    /**
     * 学期Id
     */
    @ApiModelProperty("学期Id")
    private Long termId;

    /**
     * 学期名称
     */
    @ApiModelProperty("学期名称")
    private String termName;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 学期类型（1上学期 2下学期）
     */
    @ApiModelProperty("学期类型（1上学期 2下学期）")
    private String type;
}
