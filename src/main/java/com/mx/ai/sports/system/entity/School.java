package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学校信息表，目前没有管理功能，就一条数据
 * @author Mengjiaxin
 * @date 2019-08-20 19:48
 */
@Data
@TableName("SYS_SCHOOL")
public class School implements Serializable {


    /**
     * 学校Id
     */
    @TableId(value = "SCHOOL_ID", type = IdType.AUTO)
    private Long schoolId;

    /**
     * 学校名称
     */
    @TableField("SCHOOL_NAME")
    private String schoolName;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

}
