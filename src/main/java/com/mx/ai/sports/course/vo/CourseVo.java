package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;

/**
 * 课程信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class CourseVo implements Serializable {

    private static final long serialVersionUID = -5325593143888681537L;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程编号")
    private String courseNo;

    @ApiModelProperty("发布的老师Id")
    private Long userId;

    @ApiModelProperty("发布的老师姓名")
    private String fullName;

    @ApiModelProperty("星期，分别用 1周一 2周二 ... 周天7 格式： 1,2,3,4")
    private String week;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("课程的开始时间")
    private Time startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("课程的结束时间")
    private Time endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("课程打卡的开始时间")
    private Time signedStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("课程打卡的结束时间")
    private Time signedEndTime;

    @ApiModelProperty("打卡的坐标点")
    private String location;

    @ApiModelProperty("坐标点的别名")
    private String locationName;

    @ApiModelProperty("以坐标点打卡的范围，以米为单位")
    private Long range;

    @ApiModelProperty("课程对应的图片")
    private String images;

    @ApiModelProperty("是否是今日课程")
    private Boolean isToday;
}
