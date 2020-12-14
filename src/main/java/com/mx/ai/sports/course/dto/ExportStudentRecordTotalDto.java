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
public class ExportStudentRecordTotalDto implements Serializable {


    private static final long serialVersionUID = -6653378537746501601L;

    @ExcelField(title = "学号", order = 1)
    private String sno;

    @ExcelField(title = "姓名", order = 2)
    private String fullName;

    @ExcelField(title = "班级", order = 3)
    private String classesName;

    @ExcelField(title = "总成绩", order = 4)
    private long score;

    @ExcelField(title = "1", order = 5)
    private String name1;

    @ExcelField(title = "2", order = 6)
    private String name2;

    @ExcelField(title = "3", order = 7)
    private String name3;

    @ExcelField(title = "4", order = 8)
    private String name4;

    @ExcelField(title = "5", order = 9)
    private String name5;

    @ExcelField(title = "6", order = 10)
    private String name6;

    @ExcelField(title = "7", order = 11)
    private String name7;

    @ExcelField(title = "8", order = 12)
    private String name8;

    @ExcelField(title = "9", order = 13)
    private String name9;

    @ExcelField(title = "10", order = 14)
    private String name10;


}
