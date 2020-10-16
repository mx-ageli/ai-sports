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
public class ClassesSmallVo implements Serializable {

    private static final long serialVersionUID = 7196951188008253709L;

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
     * 头像，oss地址
     */
    @ApiModelProperty("头像，oss地址")
    private String avatar;


}
