package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.ClassesApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.query.ClassesUpdateVo;
import com.mx.ai.sports.system.service.IClassesService;
import com.mx.ai.sports.system.service.ISchoolService;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 班级相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:34 下午
 */
@Slf4j
@RestController("ClassesApi")
public class ClassesController extends BaseRestController implements ClassesApi {

    @Autowired
    private IClassesService classesService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISchoolService schoolService;

    @Override
    @TeacherRole
    @Log("老师创建班级")
    public AiSportsResponse<Boolean> add(@RequestBody @Valid ClassesUpdateVo classes) throws AiSportsException {
        // 先查询是否已经存在
        Classes classesAdd = classesService.findByClassesName(classes.getClassesName());
        if (classesAdd != null) {
            return new AiSportsResponse<Boolean>().message("班级已经存在！").fail().data(Boolean.FALSE);
        }
        UserSimple userSimple = getCurrentUser();
        // 创建班级，以及添加老师班级的关系
        return new AiSportsResponse<Boolean>().success().data(classesService.saveClasses(classes, userSimple));
    }

    @Override
    @TeacherRole
    @Log("老师修改班级")
    public AiSportsResponse<Boolean> update(@RequestBody @Valid ClassesUpdateVo classes) throws AiSportsException {
        // 先查询这个班级是否是当前用户创建的
        Classes classesUpdate = classesService.getById(classes.getClassesId());
        if (classesUpdate == null) {
            return new AiSportsResponse<Boolean>().message("班级Id错误，没有查询到班级数据！").fail().data(Boolean.FALSE);
        } else if (Objects.equals(classesUpdate.getClassesName(), classes.getClassesName()) && Objects.equals(classesUpdate.getAvatar(), classes.getAvatar())) {
            // 如果修改的名称与数据库中名称一致，则不需要重复修改
            return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
        }

        // 还需要判断修改的名称与其他的班级是否冲突
        Classes otherClass = classesService.findByClassesNameNotClassesId(classes.getClassesId(), classes.getClassesName());
        if (otherClass != null) {
            return new AiSportsResponse<Boolean>().message("修改的班级名称与其他班级名称冲突！").fail().data(Boolean.FALSE);
        }

        classesUpdate.setAvatar(classes.getAvatar());
        classesUpdate.setClassesName(classes.getClassesName());
        return new AiSportsResponse<Boolean>().success().data(classesService.updateById(classesUpdate));
    }

    @Override
//    @Log("查询班级列表")
    public AiSportsResponse<List<ClassesSmallVo>> findBySchoolId(@NotNull @RequestParam("schoolId") Long schoolId) {
        School school = schoolService.getById(schoolId);
        if (school == null) {
            return new AiSportsResponse<List<ClassesSmallVo>>().fail().message("学校Id错误！");
        }
        return new AiSportsResponse<List<ClassesSmallVo>>().success().data(classesService.findBySchoolId(schoolId));
    }

    @Override
//    @Log("根据班级Id查询班级详情")
    public AiSportsResponse<ClassesSmallVo> findById(@NotNull @RequestParam("classesId") Long classesId) {
        return new AiSportsResponse<ClassesSmallVo>().success().data(classesService.findById(classesId));
    }

    @Override
    @TeacherRole
//    @Log("查询当前老师所创建的班级")
    public AiSportsResponse<List<ClassesSmallVo>> findByTeacher() throws AiSportsException {

        UserSimple userSimple = getCurrentUser();

        return new AiSportsResponse<List<ClassesSmallVo>>().success().data(classesService.findBySchoolIdAndUserId(userSimple.getSchoolId(), userSimple.getUserId()));
    }

    @Override
//    @Log("查询所有的学校列表")
    public AiSportsResponse<List<SchoolVo>> findSchool() {
        List<School> schools = schoolService.list();

        if (CollectionUtils.isEmpty(schools)) {
            return new AiSportsResponse<List<SchoolVo>>().fail().message("后台数据错误，还没有创建学校！");
        }
        List<SchoolVo> schoolVos = new ArrayList<>();
        schools.forEach(school -> {
            SchoolVo schoolVo = new SchoolVo();
            schoolVo.setSchoolId(school.getSchoolId());
            schoolVo.setSchoolName(school.getSchoolName());
            schoolVos.add(schoolVo);
        });

        return new AiSportsResponse<List<SchoolVo>>().success().data(schoolVos);
    }

    @Override
//    @Log("查询一个班级中的学生列表")
    public AiSportsResponse<IPage<UserSmallVo>> findStudentByClassesId(@RequestBody @Valid ClassesQuery query) {
        Classes classes = classesService.getById(query.getClassesId());
        if (classes == null) {
            return new AiSportsResponse<IPage<UserSmallVo>>().fail().message("班级Id错误，没有查询到班级数据！");
        }

        return new AiSportsResponse<IPage<UserSmallVo>>().success().data(userService.findByClassesId(query));
    }

}
