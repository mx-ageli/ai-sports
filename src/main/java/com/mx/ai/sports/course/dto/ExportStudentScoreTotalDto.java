package com.mx.ai.sports.course.dto;


import com.github.crab2died.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生成绩记录表
 * @author Mengjiaxin
 * @date 2020/12/13 下午10:58
 */
@Data
public class ExportStudentScoreTotalDto implements Serializable {

    private static final long serialVersionUID = 9164626574067782048L;

    @ExcelField(title = "学号", order = 1)
    private String sno;

    @ExcelField(title = "姓名", order = 2)
    private String fullName;

    @ExcelField(title = "班级", order = 3)
    private String classesName;

    @ExcelField(title = "AI健身50%", order = 4)
    private float aiScore;

    @ExcelField(title = "考勤50%", order = 5)
    private float signedScore;

    @ExcelField(title = "", order = 6)
    private String empty1;
    @ExcelField(title = "", order = 7)
    private String empty2;
    @ExcelField(title = "", order = 8)
    private String empty3;
    @ExcelField(title = "", order = 9)
    private String empty4;
    @ExcelField(title = "", order = 10)
    private String empty5;

    @ExcelField(title = "平时成绩", order = 11)
    private float score;



}
