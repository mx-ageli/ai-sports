package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.ClassesTeacher;

import java.util.List;

/**
 * 班级和老师的关系
 * @author Mengjiaxin
 * @date 2020/10/15 2:57 下午
 */
public interface IClassesTeacherService extends IService<ClassesTeacher> {

    List<ClassesTeacher> batchClassesTeacher(List<ClassesTeacher> classesTeachers);
}
