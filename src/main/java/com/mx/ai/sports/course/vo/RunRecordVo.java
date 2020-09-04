package com.mx.ai.sports.course.vo;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 跑步记录信息
 *
 * @author Mengjiaxin
 * @date 2020/8/5 1:51 下午
 */
@Data
public class RunRecordVo implements Serializable {


    private static final long serialVersionUID = -2360462021926074006L;

    @ApiModelProperty("范围 全部健身次数")
    private Long allCount;

    @ApiModelProperty("平均每天健身次数")
    private Float avgCount;

    @ApiModelProperty("范围 健身时长")
    private Float allRunTime;

    @ApiModelProperty("范围 健身里程")
    private Float allMileage;

    @ApiModelProperty("跑步记录信息详情")
    private IPage<RunRecordDetailVo> detailPage;


}
