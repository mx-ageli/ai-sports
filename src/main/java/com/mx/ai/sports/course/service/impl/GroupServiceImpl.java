package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.entity.Group;
import com.mx.ai.sports.course.mapper.GroupMapper;
import com.mx.ai.sports.course.service.IGroupService;
import com.mx.ai.sports.course.vo.GroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("GroupService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Override
    public Boolean batchCreate(Course course, Integer groupCount, Integer maxCount) throws AiSportsException {

        // 得出每组的上限人数
        Integer maxStudentCount = (int) Math.ceil((double) maxCount / groupCount);

        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            Group group = new Group();
            group.setUserId(course.getUserId());
            group.setMaxCount(maxStudentCount);
            group.setCreateTime(new Date());
            group.setCurrentCount(0);
            group.setCourseId(course.getCourseId());

            group.setGroupName((i + 1) + "组");
            groups.add(group);
        }

        return this.saveBatch(groups);
    }

    @Override
    public IPage<GroupVo> findByCourseId(QueryRequest request, Long courseId) {
        return null;
    }

    @Override
    public IPage<GroupVo> findByUserId(QueryRequest request, Long userId) {
        return null;
    }

    @Override
    public Group findByGroupName(Long groupId, String groupName) {
        return null;
    }

    @Override
    public Group findOne(Long courseId) {
        return null;
    }

    @Override
    public Group findCanJoinGroup(Long courseId) {

        return this.baseMapper.findCanJoinGroup(courseId);
    }

    @Override
    public Group findMyGroup(Long userId, Long courseId) {
        return this.baseMapper.findMyGroup(userId, courseId);
    }
}
