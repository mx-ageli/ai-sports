package com.mx.ai.sports.system.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 课程对应的序号以及老师
 * @author Mengjiaxin
 * @date 2020/12/13 下午10:29
 */
@Data
public class SubjectTeacherDto implements Serializable {

    private static final long serialVersionUID = -3922317571790513688L;

    /**
     * 课程Id
     */
    private Long subjectId;

    /**
     * 课程名称
     */
    private String subjectName;

    /**
     * 课程编号
     */
    private String number;

    /**
     * 课程序号Id
     */
    private Long subjectSeqId;

    /**
     * 课程序号
     */
    private String seq;

    /**
     * 老师Id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String fullName;


}
