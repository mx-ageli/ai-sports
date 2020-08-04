package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生和班级的关系表
 * @author Mengjiaxin
 * @date 2019-08-20 19:48
 */
@Data
@TableName("SYS_STUDENT_CLASSES")
public class StudentClasses implements Serializable {

    /**
     * 班级Id
     */
    @TableId(value = "CLASSES_ID")
    private Long classesId;

    /**
     * 学生Id
     */
    @TableId(value = "USER_ID")
    private Long userId;


}
