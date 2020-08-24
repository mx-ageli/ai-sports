package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * 学生的每次课程完成记录
 * @author Mengjiaxin
 * @date 2020/8/24 4:31 下午
 */
@Data
public class RecordStudentVo implements Serializable {

    private static final long serialVersionUID = 7064857589539686966L;

    @ApiModelProperty("学生的课程记录Id")
    private Long courseStudentId;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @ApiModelProperty("是否是今日课程")
    private Boolean isToday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("学生Id")
    private Long userId;

    @ApiModelProperty("上课的打卡时间")
    private Time startTime;

    @ApiModelProperty("下课的打卡时间")
    private Time endTime;

    @ApiModelProperty("是否缺席")
    private Boolean isAbsent;

    @ApiModelProperty("是否迟到")
    private Boolean isLate;

    @ApiModelProperty("是否合格")
    private Boolean isPass;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("打卡的坐标点")
    private String location;

    @ApiModelProperty("坐标点的别名")
    private String locationName;

    @ApiModelProperty("课程对应的图片")
    private String images;


}
