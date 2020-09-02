package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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

    @ApiModelProperty("发布的老师Id")
    private Long userId;

    @ApiModelProperty("发布的老师姓名")
    private String fullName;

    @ApiModelProperty("星期，分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 格式： 以逗号拼接 1,2,3,4,5,6,7")
    private String week;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @ApiModelProperty("课程的开始时间")
    private String startTime;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @ApiModelProperty("课程的结束时间")
    private String endTime;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @ApiModelProperty("课程打卡的时间")
    private String signedTime;

    @ApiModelProperty("打卡的坐标点 纬度")
    private String lat;

    @ApiModelProperty("打卡的坐标点 经度")
    private String lon;

    @ApiModelProperty("坐标点的别名")
    private String locationName;

    @ApiModelProperty("以坐标点打卡的范围，以米为单位")
    private Long scope;

    @ApiModelProperty("课程对应的图片")
    private String images;

    @ApiModelProperty("是否是今日课程")
    private Boolean isToday;

    @ApiModelProperty("课程报名状态，1可报课 2不可报 3已报课 4已结束 学生使用字段")
    private String entryStatus;

    @ApiModelProperty("课程状态，1正常,2暂停")
    private String status;

    @ApiModelProperty("报名人数")
    private Long allCount;
}
