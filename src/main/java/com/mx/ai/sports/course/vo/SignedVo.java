package com.mx.ai.sports.course.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 打卡信息查询
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class SignedVo implements Serializable {

    private static final long serialVersionUID = 7302852008068871587L;

    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("学生Id")
    private Long userId;

    @ApiModelProperty("上课打卡时间")
    private Date startTime;

    @ApiModelProperty("下课打卡时间")
    private Date endTime;

    @ApiModelProperty("是否迟到")
    private Boolean isLate;

    @ApiModelProperty("打卡的坐标点 纬度")
    private String lat;

    @ApiModelProperty("打卡的坐标点 经度")
    private String lon;

    @ApiModelProperty("坐标点的名称")
    private String locationName;


}
