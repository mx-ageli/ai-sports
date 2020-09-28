package com.mx.ai.sports.course.query;


import com.mx.ai.sports.common.annotation.IsTime;
import com.mx.ai.sports.common.annotation.IsWeek;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增课程信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class CourseAddVo implements Serializable {

    private static final long serialVersionUID = 4576569425239953405L;

    /**
     * 课程名称
     */
    @NotNull(message = "课程名称不能为null")
    @ApiModelProperty("课程名称")
    private String courseName;

    /**
     * 星期，分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 ... 周天7 格式： 以逗号拼接 1,2,3,4,5,6,7
     */
    @NotNull(message = "星期不能为null")
    @IsWeek(message = "星期不能为空，或者时间格式不正确， 格式： 以逗号拼接 1,2,3,4,5,6,7")
    @ApiModelProperty("星期，分别用 1周日 2周一 3周二 4周三 5周四 6周五 7周六 ... 周天7 格式： 以逗号拼接 1,2,3,4,5,6,7")
    private String week;

    /**
     * 课程的开始时间
     */
    @NotNull(message = "课程的开始时间不能为null")
    @IsTime(message = "课程开始时间格式不正确, 格式：HH:mm")
    @ApiModelProperty("课程开始时间")
    private String startTime;

    /**
     * 课程的结束时间
     */
    @NotNull(message = "课程的结束时间不能为null")
    @IsTime(message = "课程结束时间格式不正确, 格式：HH:mm")
    @ApiModelProperty("课程结束时间")
    private String endTime;

    /**
     * 课程打卡的时间
     */
    @NotNull(message = "课程打卡的时间不能为null")
    @IsTime(message = "课程打卡的时间格式不正确, 格式：HH:mm")
    @ApiModelProperty("课程打卡的时间")
    private String signedTime;

    /**
     * 打卡的坐标点 纬度
     */
    @NotNull(message = "纬度不能为null")
    @ApiModelProperty("打卡的坐标点 纬度")
    private String lat;

    /**
     * 打卡的坐标点 经度
     */
    @NotNull(message = "经度不能为null")
    @ApiModelProperty("打卡的坐标点 经度")
    private String lon;

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
    private Long scope;

    /**
     * 课程对应的图片
     */
    @NotNull(message = "课程对应的图片不能为null")
    @ApiModelProperty("课程对应的图片")
    private String images;

    /**
     * 课程状态 1=正常,2=暂停
     */
    @ApiModelProperty("课程状态 1=正常,2=暂停")
    private String status;

    /**
     * 课程简介
     */
    @NotNull(message = "课程简介不能为null")
    @ApiModelProperty("课程简介")
    private String content;

    /**
     * 是否为跑步课程
     */
    @NotNull(message = "是否为跑步课程不能为null")
    @ApiModelProperty("是否为跑步课程")
    private Boolean isRun;

    @NotNull(message = "课程的上限人数不能为null")
    @ApiModelProperty("课程的上限人数")
    private Integer maxCount;

    @NotNull(message = "小组数量不能为null")
    @ApiModelProperty("小组数量")
    private Integer groupCount;
}
