package com.mx.ai.sports.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * APP更新
 * @author Mengjiaxin
 * @date 2020/9/9 2:16 下午
 */
@Data
@TableName("SYS_REPLACE")
public class Replace implements Serializable {

    private static final long serialVersionUID = 727938073180269132L;

    /**
     * 更新Id
     */
    @TableId(value = "REPLACE_ID", type = IdType.AUTO)
    private Long replaceId;

    /**
     * 更新说明
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 是否强制更新
     */
    @TableId(value = "IS_FORCE_UP")
    private Boolean isForceUp;

    /**
     * 发布时间
     */
    @TableId(value = "RELEASE_TIME")
    private Date releaseTime;

    /**
     * apk下载地址
     */
    @TableField("UPDATE_URL")
    private String updateUrl;

    /**
     * 版本号 字符
     */
    @TableField("VERSION")
    private String version;

    /**
     * 版本号 数字
     */
    @TableField("VERSION_CODE")
    private Integer versionCode;

    /**
     * 移动端的类型 1苹果 2安卓
     */
    @TableField("TYPE")
    private String type;


}
