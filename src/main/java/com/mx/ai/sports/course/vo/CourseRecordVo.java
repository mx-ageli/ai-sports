package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程完成记录
 * @author Mengjiaxin
 * @date 2020/8/24 3:23 下午
 */
@Data
public class CourseRecordVo implements Serializable {

    private static final long serialVersionUID = -3011303060347826688L;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @ApiModelProperty("是否是今日课程")
    private Boolean isToday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("当前这次参加的总人数")
    private Long allCount;

    @ApiModelProperty("打卡人数")
    private Long singedCount;

    @ApiModelProperty("缺席人数")
    private Long absentCount;

    @ApiModelProperty("迟到人数")
    private Long lateCount;

    @ApiModelProperty("合格人数")
    private Long passCount;

    @ApiModelProperty("不合格人数")
    private Long noPassCount;


}
