package com.mx.ai.sports.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mx.ai.sports.app.api.GroupApi;
import com.mx.ai.sports.common.annotation.TeacherRole;
import com.mx.ai.sports.common.controller.BaseRestController;
import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Group;
import com.mx.ai.sports.course.query.*;
import com.mx.ai.sports.course.service.IGroupService;
import com.mx.ai.sports.course.service.IGroupStudentService;
import com.mx.ai.sports.course.vo.GroupStudentVo;
import com.mx.ai.sports.course.vo.GroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;


/**
 * 小组相关接口
 *
 * @author Mengjiaxin
 * @date 2020/8/3 2:34 下午
 */
@Slf4j
@RestController("GroupApi")
public class GroupController extends BaseRestController implements GroupApi {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupStudentService groupStudentService;

    @Override
    public AiSportsResponse<IPage<GroupVo>> findByCourseId(@RequestBody @Valid CourseQuery query) throws AiSportsException {

        return new AiSportsResponse<IPage<GroupVo>>().success().data(groupService.findByCourseId(query.getRequest(), query.getCourseId()));
    }

    @Override
    public AiSportsResponse<IPage<GroupVo>> findByUserId(@RequestBody @Valid QueryRequest request) throws AiSportsException {
        return new AiSportsResponse<IPage<GroupVo>>().success().data(groupService.findByUserId(request, getCurrentUserId()));
    }

    @Override
    public AiSportsResponse<IPage<GroupStudentVo>> findStudentByCourseId(@RequestBody @Valid GroupQuery query) throws AiSportsException {
        return new AiSportsResponse<IPage<GroupStudentVo>>().success().data(groupStudentService.findByGroupId(query.getRequest(), query.getGroupId()));
    }

    @Override
    public AiSportsResponse<Boolean> updateStudent(@RequestBody @Valid GroupStudentUpdateVo updateVo) throws AiSportsException {

        return new AiSportsResponse<Boolean>().success().data(groupStudentService.updateStudent(updateVo));
    }

    @Override
    @TeacherRole
    public AiSportsResponse<Boolean> add(@RequestBody @Valid GroupAddVo addVo) throws AiSportsException {
        // TODO 查询小组名称是否重名


        Group group = new Group();
        group.setCourseId(addVo.getCourseId());
        group.setGroupName(addVo.getGroupName());

        group.setCurrentCount(0);
        group.setCreateTime(new Date());
        group.setUserId(getCurrentUserId());

        if (addVo.getMaxCount() != null && addVo.getMaxCount() > 0) {
            group.setMaxCount(addVo.getMaxCount());
        } else {
            // TODO 根据课程ID查询其他小组的上限人数

        }

        return new AiSportsResponse<Boolean>().success().data(groupService.save(group));
    }

    @Override
    @TeacherRole
    public AiSportsResponse<Boolean> update(@RequestBody @Valid GroupUpdateVo updateVo) throws AiSportsException {
        Group group = groupService.getById(updateVo.getGroupId());
        if (group == null) {
            return new AiSportsResponse<Boolean>().fail().message("小组Id错误，没有查询到数据");
        }
        // 查询当前这个小组名称是否与其他小组重名
        Group repeatGroup = groupService.findByGroupName(updateVo.getGroupId(), updateVo.getGroupName());
        if (repeatGroup != null) {
            return new AiSportsResponse<Boolean>().fail().message("小组名称与其他小组名称冲突");
        }
        // 新的小组名称
        group.setGroupName(updateVo.getGroupName());
        return new AiSportsResponse<Boolean>().success().data(groupService.saveOrUpdate(group));
    }

    @Override
    @TeacherRole
    public AiSportsResponse<Boolean> remove(@NotNull @RequestParam("groupId") Long groupId) throws AiSportsException {
        Group group = groupService.getById(groupId);
        if (group == null) {
            return new AiSportsResponse<Boolean>().fail().message("小组Id错误，没有查询到数据");
        }
        if (!Objects.equals(group.getUserId(), getCurrentUserId())) {
            return new AiSportsResponse<Boolean>().fail().message("当前登录用户不是小组的创建者，不能删除");
        }
        // 查询小组内有多少个学生
        Long studentCount = groupStudentService.countStudent(groupId);
        // 如果学生数量大于0，说明小组内有人，不能删除
        if (studentCount > 0) {
            return new AiSportsResponse<Boolean>().fail().message("小组内还有学生，不能删除");
        }
        return new AiSportsResponse<Boolean>().success().data(groupService.removeById(groupId));
    }
}
