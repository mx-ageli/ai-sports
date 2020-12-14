package com.mx.ai.sports.course.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 学生课程记录统计
 * @author Mengjiaxin
 * @date 2020/12/13 上午12:51
 */
@Data
public class StudentRecordTotalDto implements Serializable {

    private static final long serialVersionUID = 4631145595294064730L;

    private Long userId;

    private String sno;

    private String fullName;

    private Long classId;

    private String classesName;

    private Long subjectId;

    private Long subjectSeqId;

    private Long courseId;

    private String courseName;

    private String isPass;


}
