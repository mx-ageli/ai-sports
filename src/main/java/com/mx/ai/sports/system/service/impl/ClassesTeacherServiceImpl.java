package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.ClassesTeacher;
import com.mx.ai.sports.system.mapper.ClassesTeacherMapper;
import com.mx.ai.sports.system.service.IClassesTeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 班级Service
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:58
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ClassesTeacherServiceImpl extends ServiceImpl<ClassesTeacherMapper, ClassesTeacher> implements IClassesTeacherService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ClassesTeacher> batchClassesTeacher(List<ClassesTeacher> classesTeachers) {
        // 先把老的数据查询出来
        List<ClassesTeacher> oldClassesTeachers = this.list();

        for (ClassesTeacher oldClassesTeacher : oldClassesTeachers) {
            classesTeachers.removeIf(classesTeacher -> Objects.equals(oldClassesTeacher.getClassesId(), classesTeacher.getClassesId())
                    && Objects.equals(oldClassesTeacher.getUserId(), classesTeacher.getUserId()));

        }

        if (!CollectionUtils.isEmpty(classesTeachers)) {
            saveBatch(classesTeachers);

            oldClassesTeachers.addAll(classesTeachers);
        }

        return oldClassesTeachers;
    }
}
