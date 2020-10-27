package com.mx.ai.sports.course.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mx.ai.sports.course.entity.Group;

/**
 *
 * @author Mengjiaxin
 * @date 2020/9/25 1:57 下午
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 查询一个课程中还可以加入的小组，小组的上限人数>组内人数的小组
     * @param courseId
     * @return
     */
    Group findCanJoinGroup(Long courseId);

    /**
     * 查询我所在的小组信息
     * @param userId
     * @param courseId
     * @return
     */
    Group findMyGroup(Long userId, Long courseId);

}