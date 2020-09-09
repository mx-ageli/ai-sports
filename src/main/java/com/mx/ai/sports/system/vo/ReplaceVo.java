package com.mx.ai.sports.system.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统版本更新
 *
 * @author Mengjiaxin
 * @date 2020/9/9 2:37 下午
 */
@Data
public class ReplaceVo implements Serializable {

    private static final long serialVersionUID = -5002251772025244705L;

    @ApiModelProperty("更新说明")
    private String content;

    @ApiModelProperty("是否强制更新")
    private Boolean isForceUp;

    @ApiModelProperty("发布时间")
    private Date releaseTime;

    @ApiModelProperty("apk下载地址")
    private String updateUrl;

    @ApiModelProperty("版本号 字符")
    private String version;

    @ApiModelProperty("版本号 数字")
    private String versionCode;

}
