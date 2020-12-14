package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.dto.SubjectTeacherDto;
import com.mx.ai.sports.system.entity.Subject;

import java.util.List;

/**
 * 主课程
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:23 下午
 */
public interface ISubjectService extends IService<Subject> {


    /**
     * 批量的保存主课程,如果有已经存在的课程名称就不保存
     *
     * @param subjects 所有已经存在的课程
     * @return
     */
    List<Subject> batchSubject(List<Subject> subjects);

    /**
     * 查询所有的课程序号以及老师
     * @return
     */
    List<SubjectTeacherDto> findSubjectTeacherDto();
}
