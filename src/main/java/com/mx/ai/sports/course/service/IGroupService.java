package com.mx.ai.sports.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.Group;
import com.mx.ai.sports.course.vo.GroupVo;


/**
 * 课程小组service
 *
 * @author Mengjiaxin
 * @date 2020/9/25 1:58 下午
 */
public interface IGroupService extends IService<Group> {

    /**
     * 批量的创建小组
     *
     * @param course     课程信息
     * @param groupCount 需要创建的数量
     * @param maxCount   课程上限人数
     * @return
     */
    Boolean batchCreate(Course course, Integer groupCount, Integer maxCount) throws AiSportsException;

    /**
     * 根据课程查询小组列表
     *
     * @param request  分页条件
     * @param courseId
     * @return
     */
    IPage<GroupVo> findByCourseId(QueryRequest request, Long courseId);

    /**
     * 根据老师去查询所有课程对应的小组列表
     *
     * @param request 分页条件
     * @param userId  老师Id
     * @return
     */
    IPage<GroupVo> findByUserId(QueryRequest request, Long userId);

    /**
     * 查询当前这个小组名称是否与其他小组重名
     * @param groupId
     * @param groupName
     * @return
     */
    Group findByGroupName(Long groupId, String groupName);

    /**
     *
     * @param courseId
     * @return
     */
    Group findOne(Long courseId);

    /**
     *
     * @param courseId
     * @return
     */
    Group findCanJoinGroup(Long courseId);

    Group findMyGroup(Long userId, Long courseId);

}
