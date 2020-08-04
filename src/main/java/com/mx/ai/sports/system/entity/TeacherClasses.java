package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 老师和班级的关系表
 * @author Mengjiaxin
 * @date 2020/8/3 5:10 下午
 */
@Data
@TableName("SYS_TEACHER_CLASSES")
public class TeacherClasses implements Serializable {

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
