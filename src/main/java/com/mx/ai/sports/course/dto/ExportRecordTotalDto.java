package com.mx.ai.sports.course.dto;


import com.github.crab2died.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程记录总览
 *
 * @author Mengjiaxin
 * @date 2020/9/18 10:45 上午
 */
@Data
public class ExportRecordTotalDto implements Serializable {

    private static final long serialVersionUID = -3922317571790513688L;

    private Long courseId;

    @ExcelField(title = "课程名称", order = 2)
    private String courseName;

    @ExcelField(title = "老师", order = 3)
    private String teacherName;

    @ExcelField(title = "星期", order = 4)
    private String week;

    @ExcelField(title = "开始时间", order = 5)
    private String startTime;

    @ExcelField(title = "结束时间", order = 6)
    private String endTime;

    @ExcelField(title = "当前报名人数", order = 7)
    private Long currentCount;

    @ExcelField(title = "累计上课次数", order = 7)
    private Long courseCount;

    @ExcelField(title = "累计打卡次数", order = 8)
    private Long signedCount;

    @ExcelField(title = "累计缺席人数", order = 9)
    private Long absentCount;

    @ExcelField(title = "累计迟到人数", order = 10)
    private Long lateCount;

    @ExcelField(title = "累计跑步人数", order = 11)
    private Long runCount;

    @ExcelField(title = "累计合格人数", order = 12)
    private Long passCount;

    @ExcelField(title = "累计不合格人数", order = 13)
    private Long noPassCount;


}
