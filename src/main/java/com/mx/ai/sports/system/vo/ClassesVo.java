package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班级信息
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class ClassesVo implements Serializable {

    private static final long serialVersionUID = -3964377585818464531L;

    /**
     * 班级Id
     */
    @ApiModelProperty("班级Id")
    private Long classesId;

    /**
     * 班级名称
     */
    @ApiModelProperty("班级名称")
    private String classesName;

    /**
     * 班主任的用户Id
     */
    @ApiModelProperty("班主任的用户Id")
    private Long userId;

    /**
     * 老师姓名
     */
    @ApiModelProperty("老师姓名")
    private String teacherName;

    /**
     * 头像，oss地址
     */
    @ApiModelProperty("头像，oss地址")
    private String avatar;


    public ClassesVo(){

    }

    public ClassesVo(ClassesSmallVo classesSmallVo){
        this.classesId = classesSmallVo.getClassesId();
        this.classesName = classesSmallVo.getClassesName();
        this.avatar = classesSmallVo.getAvatar();
    }
}
