package com.mx.ai.sports.course.query;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;

/**
 * 课程信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class CourseUpdateVo implements Serializable {

    private static final long serialVersionUID = 315119736703562498L;

    /**
     * 课程Id, 新增不传，修改传
     */
    @ApiModelProperty("课程Id, 新增不传，修改传")
    private Long courseId;

    /**
     * 课程名称
     */
    @NotNull(message = "课程名称不能为null")
    @ApiModelProperty("课程名称")
    private String courseName;

    /**
     * 课程编号
     */
    @NotNull(message = "课程编号不能为null")
    @ApiModelProperty("课程编号")
    private String courseNo;

    /**
     * 星期，分别用 1周一 2周二 ... 周天7 格式： 1,2,3,4
     */
    @NotNull(message = "星期不能为null")
    @ApiModelProperty("星期，分别用 1周一 2周二 ... 周天7 格式： 1,2,3,4")
    private String week;

    /**
     * 课程的开始时间
     */
    @NotNull(message = "课程的开始时间不能为null")
    @ApiModelProperty("课程的开始时间")
    private Time startTime;

    /**
     * 课程的结束时间
     */
    @NotNull(message = "课程的结束时间不能为null")
    @ApiModelProperty("课程的结束时间")
    private Time endTime;

    /**
     * 课程打卡的开始时间
     */
    @NotNull(message = "课程打卡的开始时间不能为null")
    @ApiModelProperty("课程打卡的开始时间")
    private Time signedStartTime;

    /**
     * 课程打卡的结束时间
     */
    @NotNull(message = "课程打卡的结束时间不能为null")
    @ApiModelProperty("课程打卡的结束时间")
    private Time signedEndTime;

    /**
     * 打卡的坐标点
     */
    @NotNull(message = "打卡的坐标点不能为null")
    @ApiModelProperty("打卡的坐标点")
    private String location;

    /**
     * 坐标点的别名
     */
    @NotNull(message = "坐标点的别名不能为null")
    @ApiModelProperty("坐标点的别名")
    private String locationName;

    /**
     * 以坐标点打卡的范围，以米为单位
     */
    @NotNull(message = "以坐标点打卡的范围不能为null")
    @ApiModelProperty("以坐标点打卡的范围，以米为单位")
    private Long range;

    /**
     * 课程对应的图片
     */
    @NotNull(message = "课程对应的图片不能为null")
    @ApiModelProperty("课程对应的图片")
    private String images;

}
