package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.ClassesTeacher;
import com.mx.ai.sports.system.entity.SubjectTeacher;
import com.mx.ai.sports.system.query.ClassesUpdateVo;
import com.mx.ai.sports.system.service.IClassesTeacherService;
import com.mx.ai.sports.system.vo.ClassesSmallVo;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.mapper.ClassesMapper;
import com.mx.ai.sports.system.service.IClassesService;
import com.mx.ai.sports.system.vo.UserSimple;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {

    @Autowired
    private IClassesTeacherService classesTeacherService;

    @Override
    public Classes findByClassesName(String classesName) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<Classes>().eq(Classes::getClassesName, classesName));
    }

    @Override
    public List<ClassesSmallVo> findBySchoolId(Long schoolId) {
        return baseMapper.findBySchoolId(schoolId);
    }

    @Override
    public List<ClassesSmallVo> findBySchoolIdAndUserId(Long schoolId, Long userId) {
        return baseMapper.findBySchoolIdAndUserId(schoolId, userId);
    }

    @Override
    public Classes findByClassesNameNotClassesId(Long classesId, String classesName) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<Classes>().eq(Classes::getClassesName, classesName).ne(Classes::getClassesId, classesId));
    }

    @Override
    public ClassesSmallVo findById(Long classesId) {
        return baseMapper.findById(classesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Classes> batchClasses(List<Classes> classesList) {
        // 先把老的数据查询出来
        List<Classes> oldClassesList = this.list();

        for (Classes oldClasses : oldClassesList) {
            classesList.removeIf(classes -> Objects.equals(oldClasses.getClassesName(), classes.getClassesName()));
        }

        if (!CollectionUtils.isEmpty(classesList)) {
            saveBatch(classesList);

            oldClassesList.addAll(classesList);
        }

        return oldClassesList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveClasses(ClassesUpdateVo classes, UserSimple userSimple) {
        Classes classesAdd = new Classes();
        classesAdd.setClassesName(classes.getClassesName());
        classesAdd.setAvatar(classes.getAvatar());
        classesAdd.setSchoolId(userSimple.getSchoolId());

        this.save(classesAdd);
        // 再保存老师班级关系
        ClassesTeacher classesTeacher = new ClassesTeacher();
        classesTeacher.setUserId(userSimple.getUserId());
        classesTeacher.setClassesId(classesAdd.getClassesId());

        return classesTeacherService.save(classesTeacher);
    }

}
