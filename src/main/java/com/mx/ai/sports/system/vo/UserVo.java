package com.mx.ai.sports.system.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = -3964377585818464531L;

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("用户名，电话号码")
    private String username;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date modifyTime;

    @ApiModelProperty("最近一次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty("性别 1男 2女")
    private String sex;

    @ApiModelProperty("头像，oss地址")
    private String avatar;

    @ApiModelProperty("姓名")
    private String fullName;

    @ApiModelProperty("设备Id")
    private String deviceId;

    @ApiModelProperty("设备类型 1苹果 2安卓")
    private String deviceType;

    @ApiModelProperty("角色Id 1 学生，2 老师")
    private Long roleId;

    @ApiModelProperty("学校Id")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @JsonIgnore
    @ApiModelProperty("班级Id")
    private Long classesId;

    @ApiModelProperty("学号")
    private String sno;

    @ApiModelProperty("班级信息，只有学生才有值")
    private ClassesVo classes;

    @ApiModelProperty("学生的主课信息，只有学生才有值")
    private SubjectStudentVo subjectStudent;

    @ApiModelProperty("参加的课程数量")
    private Long courseCount = 0L;

    @ApiModelProperty("参加课程的合格数量")
    private Long passCount = 0L;


}
