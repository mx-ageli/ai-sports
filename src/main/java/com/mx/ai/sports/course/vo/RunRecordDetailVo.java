package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 跑步记录信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunRecordDetailVo implements Serializable {


    private static final long serialVersionUID = -2360462021926074006L;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程对应的图片")
    private String images;

    @ApiModelProperty("课程的坐标点 纬度")
    private String lat;

    @ApiModelProperty("课程的坐标点 经度")
    private String lon;

    @ApiModelProperty("课程的坐标点的别名")
    private String locationName;

    @ApiModelProperty("跑步状态： 1合格 2不合格")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("跑步时长(以秒为单位)")
    private Long runTime;

    @ApiModelProperty("里程")
    private Float mileage;

    @ApiModelProperty("规则设定里程")
    private Float ruleMileage;

    @ApiModelProperty("规则设定跑步时长")
    private Long ruleRunTime;


}
