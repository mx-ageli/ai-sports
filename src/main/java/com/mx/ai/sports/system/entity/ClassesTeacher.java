package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 班级和老师的对应关系
 * @author Mengjiaxin
 * @date 2020/10/15 2:51 下午
 */
@Data
@TableName("SYS_CLASSES_TEACHER")
public class ClassesTeacher implements Serializable {


    private static final long serialVersionUID = -8592352346579963818L;
    /**
     * 班级Id
     */
    @TableId(value = "CLASSES_ID")
    private Long classesId;

    /**
     * 老师Id
     */
    @TableId(value = "USER_ID")
    private Long userId;

}
