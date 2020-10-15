package com.mx.ai.sports.system.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 学生信息导入转换的对象
 * @author Mengjiaxin
 * @date 2020/10/14 2:07 下午
 */
@Data
public class ImportStudentDto implements Serializable {

    private static final long serialVersionUID = -3922317571790513688L;

    /**
     * 课程号
     */
    private String subjectNo;

    /**
     * 课程名称
     */
    private String subjectName;

    /**
     * 课序号
     */
    private String subjectSeq;

    /**
     * 班级
     */
    private String classesName;

    /**
     * 学号
     */
    private String sno;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 任课教师
     */
    private String teacherName;

}
