package com.mx.ai.sports.course.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 健身记录信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class KeepRecordVo implements Serializable {

    private static final long serialVersionUID = 5512326180983081366L;

    @ApiModelProperty("课程Id")
    private Long courseId;

    @ApiModelProperty("课程记录Id")
    private Long courseRecordId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程对应的图片")
    private String images;

    @ApiModelProperty("健身状态： 1合格 2不合格")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;


}
