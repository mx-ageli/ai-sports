package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 老师的注册登记表，这个表存在的用户才能是老师
 *
 * @author Mengjiaxin
 * @date 2020/8/5 3:48 下午
 */
@Data
@TableName("SYS_TEACHER_REGISTER")
public class TeacherRegister implements Serializable {

    /**
     * 主键Id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableId("USERNAME")
    private String username;

    /**
     * 姓名
     */
    @TableField("FULL_NAME")
    private String fullName;

    /**
     * 是否已经注册
     */
    @TableField("IS_REGISTER")
    private Boolean isRegister;
}
