package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 临时学生信息
 *
 * @author Mengjiaxin
 * @date 2020/10/15 4:48 下午
 */
@Data
public class TempStudentVo implements Serializable {

    private static final long serialVersionUID = 3027470772470140419L;

    @ApiModelProperty("临时学生Id")
    private Long tempStudentId;

    @ApiModelProperty("姓名")
    private String fullName;

    @ApiModelProperty("学号")
    private String sno;

    @ApiModelProperty("性别 1男 2女")
    private String sex;

    @ApiModelProperty("学校Id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("班级Id")
    private Long classesId;

    @ApiModelProperty("班级名称")
    private String classesName;

    @ApiModelProperty("主课Id")
    private Long subjectId;

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
