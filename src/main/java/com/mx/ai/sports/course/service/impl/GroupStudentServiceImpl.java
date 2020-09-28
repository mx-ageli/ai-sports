package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.course.entity.GroupStudent;
import com.mx.ai.sports.course.mapper.GroupStudentMapper;
import com.mx.ai.sports.course.query.GroupStudentUpdateVo;
import com.mx.ai.sports.course.service.IGroupStudentService;
import com.mx.ai.sports.course.vo.GroupStudentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("GroupStudentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GroupStudentServiceImpl extends ServiceImpl<GroupStudentMapper, GroupStudent> implements IGroupStudentService {


    @Override
    public Boolean saveGroupStudent(Long courseId, Long userId) {
        return null;
    }

    @Override
    public Boolean updateGroupStudent(Long groupId, Long userId) {
        return null;
    }

    @Override
    public IPage<GroupStudentVo> findByGroupId(QueryRequest request, Long groupId) {
        return null;
    }

    @Override
    public Boolean updateStudent(GroupStudentUpdateVo updateVo) {
        return null;
    }

    @Override
    public Long countStudent(Long groupId) {
        return null;
    }
}
