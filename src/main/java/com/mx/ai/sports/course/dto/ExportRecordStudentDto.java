package com.mx.ai.sports.course.dto;


import com.github.crab2died.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程记录学生明细
 *
 * @author Mengjiaxin
 * @date 2020/9/18 10:45 上午
 */
@Data
public class ExportRecordStudentDto implements Serializable {

    private static final long serialVersionUID = -3922317571790513688L;

    private Long courseId;

    private Long userId;

    private String courseName;

    @ExcelField(title = "学生姓名", order = 1)
    private String studentName;

    @ExcelField(title = "手机号", order = 3)
    private String phone;

    @ExcelField(title = "累计上课次数", order = 7)
    private Long courseCount;

    @ExcelField(title = "累计打卡次数", order = 8)
    private Long signedCount;

    @ExcelField(title = "累计缺席次数", order = 9)
    private Long absentCount;

    @ExcelField(title = "累计迟到次数", order = 10)
    private Long lateCount;

    @ExcelField(title = "累计合格次数", order = 12)
    private Long passCount;


}
