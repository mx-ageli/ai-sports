package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.SubjectSeq;
import com.mx.ai.sports.system.entity.SubjectTeacher;

import java.util.List;

/**
 * 主课程序号对应的用户
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:23 下午
 */
public interface ISubjectTeacherService extends IService<SubjectTeacher> {

    List<SubjectTeacher> batchSubjectTeacher(List<SubjectTeacher> subjectTeachers);

}
