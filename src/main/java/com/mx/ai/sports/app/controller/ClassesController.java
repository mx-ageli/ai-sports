package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.ClassesApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.query.ClassesUpdateVo;
import com.mx.ai.sports.system.service.IClassesService;
import com.mx.ai.sports.system.service.ISchoolService;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.vo.SchoolVo;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.vo.UserSmallVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
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

        classesAdd = new Classes();
        classesAdd.setClassesName(classes.getClassesName());
        classesAdd.setCreateTime(new Date());
        classesAdd.setAvatar(classes.getAvatar());
        classesAdd.setSchoolId(userSimple.getSchoolId());
        classesAdd.setUserId(userSimple.getUserId());
        classesService.save(classesAdd);

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    @TeacherRole
    @Log("老师修改班级")
    public AiSportsResponse<Boolean> update(@RequestBody @Valid ClassesUpdateVo classes) throws AiSportsException {
        // 先查询这个班级是否是当前用户创建的
        Classes classesUpdate = classesService.getById(classes.getClassesId());
        if (classesUpdate == null) {
            return new AiSportsResponse<Boolean>().message("班级Id错误，没有查询到班级数据！").fail().data(Boolean.FALSE);
        } else if (!Objects.equals(classesUpdate.getUserId(), getCurrentUserId())) {
            return new AiSportsResponse<Boolean>().message("当前登录用户没有权限操作其他老师创建的班级！").fail().data(Boolean.FALSE);
        } else if (Objects.equals(classesUpdate.getClassesName(), classes.getClassesName())) {
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
    @Log("查询班级列表")
    public AiSportsResponse<List<ClassesVo>> findBySchoolId(@NotNull @RequestParam("schoolId") Long schoolId) {
        School school = schoolService.getById(schoolId);
        if (school == null) {
            return new AiSportsResponse<List<ClassesVo>>().fail().message("学校Id错误！");
        }

        return new AiSportsResponse<List<ClassesVo>>().success().data(classesService.findBySchoolId(schoolId));
    }

    @Override
    public AiSportsResponse<ClassesVo> findById(@NotNull @RequestParam("classesId") Long classesId) {

        return new AiSportsResponse<ClassesVo>().success().data(classesService.findById(classesId));
    }

    @Override
    @TeacherRole
    public AiSportsResponse<List<ClassesVo>> findByTeacher() throws AiSportsException {

        UserSimple userSimple = getCurrentUser();

        return new AiSportsResponse<List<ClassesVo>>().success().data(classesService.findBySchoolIdAndUserId(userSimple.getSchoolId(), userSimple.getUserId()));
    }

    @Override
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
    public AiSportsResponse<IPage<UserSmallVo>> findStudentByClassesId(@RequestBody @Valid ClassesQuery query) {

        Classes classes = classesService.getById(query.getClassesId());
        if (classes == null) {
            return new AiSportsResponse<IPage<UserSmallVo>>().fail().message("班级Id错误，没有查询到班级数据！");
        }

        return new AiSportsResponse<IPage<UserSmallVo>>().success().data(userService.findByClassesId(query));
    }

}
