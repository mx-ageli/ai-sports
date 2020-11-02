package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.GroupStudent;
import com.mx.ai.sports.course.query.GroupStudentUpdateVo;
import com.mx.ai.sports.course.vo.GroupStudentVo;

/**
 * 课程小组学生service
 *
 * @author Mengjiaxin
 * @date 2020/9/25 1:58 下午
 */
public interface IGroupStudentService extends IService<GroupStudent> {

    /**
     * 学生加入课程对应小组，需要将所有小组查询出来，加入一个没有满员的小组
     *
     * @param courseId 课程Id
     * @param userId   学生Id
     * @return
     */
    Boolean saveGroupStudent(Long courseId, Long userId);

    /**
     * 更新学生的小组，给学生换组，需要判断换的组是否为同一个课程，是否满员
     *
     * @param groupId
     * @param userId
     * @return
     */
    Boolean updateGroupStudent(Long groupId, Long userId);

    /**
     * 查询一个小组内的学生列表
     *
     * @param request 分页条件
     * @param groupId
     * @return
     */
    IPage<GroupStudentVo> findByGroupId(QueryRequest request, Long groupId);

    /**
     * 老师将学生换组，只能更改到相同课程的小组
     * @param updateVo
     * @return
     */
    Boolean updateStudent(GroupStudentUpdateVo updateVo);

    /**
     * 查询小组内有多少个学生
     * @param groupId
     * @return
     */
    Long countStudent(Long groupId);

    GroupStudent findByCourseIdAndUserId(Long courseId, Long userId);

    Boolean removeByGroupIdAndUserId(Long groupId, Long userId);

    Boolean removeByUserId(Long userId);
}
