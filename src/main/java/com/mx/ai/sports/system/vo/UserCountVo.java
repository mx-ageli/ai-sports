package com.mx.ai.sports.system.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户数量统计
 *
 * @author Mengjiaxin
 * @date 2020/12/11 下午1:44
 */
@Data
public class UserCountVo implements Serializable {

    private static final long serialVersionUID = 4310206937150800658L;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("时间")
    private Date date;

    @ApiModelProperty("学生数量")
    private Long studentCount;

    @ApiModelProperty("老师数量")
    private Long teacherCount;


}
