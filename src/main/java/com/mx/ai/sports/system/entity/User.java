package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:47
 */
@Data
@TableName("SYS_USER")
public class User implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 用户Id
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名，电话号码
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    /**
     * 最近一次登录时间
     */
    @TableField("LAST_LOGIN_TIME")
    private Date lastLoginTime;

    /**
     * 性别 1男 2女
     */
    @TableField("SEX")
    private String sex;

    /**
     * 头像，oss地址
     */
    @TableField("AVATAR")
    private String avatar;

    /**
     * 姓名
     */
    @TableField("FULL_NAME")
    private String fullName;

    /**
     * 设备Id
     */
    @TableField("DEVICE_ID")
    private String deviceId;

    /**
     * 角色Id
     */
    @TableField("ROLE_ID")
    private Long roleId;

    /**
     * 学校Id
     */
    @TableField("SCHOOL_ID")
    private Long schoolId;

    /**
     * 班级Id
     */
    @TableField("CLASSES_ID")
    private Long classesId;

    /**
     * 学号
     */
    @TableField("SNO")
    private String sno;

    /**
     * 主课Id
     */
    @TableField("SUBJECT_ID")
    private Long subjectId;

    /**
     * 主课序号Id
     */
    @TableField("SUBJECT_SEQ_ID")
    private Long subjectSeqId;

    /**
     * 主课老师Id
     */
    @TableField("TEACHER_ID")
    private Long teacherId;


}
