package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.ClassesApi;
import com.mx.ai.sports.common.annotation.Log;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsConstant;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.system.query.ClassesQuery;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.ClassesVo;
import com.mx.ai.sports.system.vo.UserSimple;
import com.mx.ai.sports.system.entity.Classes;
import com.mx.ai.sports.system.service.IClassesService;
import com.mx.ai.sports.system.vo.UserSmallVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @Override
    @Log("老师创建班级")
    public AiSportsResponse<Boolean> add(@NotBlank @RequestParam("classesName") String classesName) {
        // 是否为老师用户, 不是老师直接抛异常
        if (!isTeacher()) {
            return new AiSportsResponse<Boolean>().message("当前登录用户没有权限操作！").fail().data(Boolean.FALSE);
        }
        // 先查询是否已经存在
        Classes classes = classesService.findByClassesName(classesName);
        if (classes != null) {
            return new AiSportsResponse<Boolean>().message("班级已经存在！").fail().data(Boolean.FALSE);
        }

        classes = new Classes();
        classes.setClassesName(classesName);
        classes.setCreateTime(new Date());
        classes.setSchoolId(AiSportsConstant.DEFAULT_SCHOOL_ID);
        classes.setUserId(getCurrentUserId());
        classesService.save(classes);

        return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
    }

    @Override
    @Log("老师修改班级")
    public AiSportsResponse<Boolean> update(@NotNull @RequestParam("classesId") Long classesId, @NotBlank @RequestParam("classesName") String classesName) {
        // 是否为老师用户, 不是老师直接抛异常
        if (!isTeacher()) {
            return new AiSportsResponse<Boolean>().message("当前登录用户没有权限操作！").fail().data(Boolean.FALSE);
        }
        // 先查询这个班级是否是当前用户创建的
        Classes classes = classesService.getById(classesId);
        if (classes == null) {
            return new AiSportsResponse<Boolean>().message("班级Id错误，没有查询到班级数据！").fail().data(Boolean.FALSE);
        } else if (!Objects.equals(classes.getUserId(), getCurrentUserId())) {
            return new AiSportsResponse<Boolean>().message("当前登录用户没有权限操作其他老师创建的班级！").fail().data(Boolean.FALSE);
        } else if (Objects.equals(classes.getClassesName(), classesName)) {
            // 如果修改的名称与数据库中名称一致，则不需要重复修改
            return new AiSportsResponse<Boolean>().success().data(Boolean.TRUE);
        }

        // 还需要判断修改的名称与其他的班级是否冲突
        Classes otherClass = classesService.findByClassesNameNotClassesId(classesId, classesName);
        if (otherClass != null) {
            return new AiSportsResponse<Boolean>().message("修改的班级名称与其他班级名称冲突！").fail().data(Boolean.FALSE);
        }

        classes.setClassesName(classesName);
        return new AiSportsResponse<Boolean>().success().data(classesService.updateById(classes));
    }

    @Override
    @Log("查询班级列表")
    public AiSportsResponse<List<ClassesVo>> findAll() {
        UserSimple user = getCurrentUser();

        return new AiSportsResponse<List<ClassesVo>>().success().data(classesService.findBySchoolId(user.getSchoolId()));
    }

    @Override
    public AiSportsResponse<IPage<UserSmallVo>> findStudentByClassesId(@RequestBody @Valid ClassesQuery query) {

        return new AiSportsResponse<IPage<UserSmallVo>>().success().data(userService.findByClassesId(query));
    }

}
