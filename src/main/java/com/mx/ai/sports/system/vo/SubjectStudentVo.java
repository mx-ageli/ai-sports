package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 主课信息
 * @author Mengjiaxin
 * @date 2020/10/16 10:21 上午
 */
@Data
public class SubjectStudentVo implements Serializable {

    private static final long serialVersionUID = -4274622280193695697L;

    @ApiModelProperty("主课Id")
    private Long subjectId;

    @ApiModelProperty("主课程编号")
    private String number;

    @ApiModelProperty("主课名称")
    private String subjectName;

    @ApiModelProperty("主课序号Id")
    private Long subjectSeqId;

    @ApiModelProperty("主课序号名称")
    private String seq;

    @ApiModelProperty("主课老师Id")
    private Long teacherId;

    @ApiModelProperty("主课老师名称")
    private String teacherName;

}
