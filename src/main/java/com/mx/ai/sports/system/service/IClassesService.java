package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.query.ClassesUpdateVo;
import com.mx.ai.sports.system.vo.ClassesSmallVo;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.vo.UserSimple;

import java.util.List;

/**
 * 班级Service
 *
 * @author Mengjiaxin
 * @date 2020/8/5 5:52 下午
 */
public interface IClassesService extends IService<Classes> {

    /**
     * 通过班级名称查找班级
     *
     * @param classesName 班级名称
     * @return
     */
    Classes findByClassesName(String classesName);

    /**
     * 通过学校Id查询班级列表
     *
     * @param schoolId 学校Id
     * @return
     */
    List<ClassesSmallVo> findBySchoolId(Long schoolId);

    /**
     * 通过学校Id和创建人查询班级列表
     *
     * @param schoolId 学校Id
     * @return
     */
    List<ClassesSmallVo> findBySchoolIdAndUserId(Long schoolId, Long userId);

    /**
     * 通过班级名称查找班级, 排除传入的classesId
     *
     * @param classesId   排除classesId
     * @param classesName 班级名称
     * @return
     */
    Classes findByClassesNameNotClassesId(Long classesId, String classesName);


    ClassesSmallVo findById(Long classesId);

    /**
     * 批量的导入班级信息
     * @param classesList
     * @return
     */
    List<Classes> batchClasses(List<Classes> classesList);

    /**
     * 创建班级，以及添加老师班级的关系
     * @param classes
     * @param userSimple
     * @return
     */
    Boolean saveClasses(ClassesUpdateVo classes, UserSimple userSimple);
}
