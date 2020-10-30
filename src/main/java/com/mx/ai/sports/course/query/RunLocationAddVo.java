package com.mx.ai.sports.course.query;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增跑步记录坐标
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunLocationAddVo implements Serializable {

    private static final long serialVersionUID = -4433270319120693041L;

    @NotNull(message = "纬度不能为null")
    @ApiModelProperty("跑步结束的坐标点 纬度")
    private String lat;

    @NotNull(message = "经度不能为null")
    @ApiModelProperty("跑步结束的坐标点 经度")
    private String lon;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "时间不能为null")
    @ApiModelProperty("时间")
    private Date time;


}
