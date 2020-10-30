package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.utils.JedisPoolUtil;
import com.mx.ai.sports.course.dto.CourseStudentCountDto;
import com.mx.ai.sports.course.entity.CourseStudent;
import com.mx.ai.sports.course.entity.Group;
import com.mx.ai.sports.course.entity.GroupStudent;
import com.mx.ai.sports.course.mapper.CourseStudentMapper;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.course.service.IGroupService;
import com.mx.ai.sports.course.service.IGroupStudentService;
import com.mx.ai.sports.course.vo.CourseEntryVo;
import com.mx.ai.sports.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.mx.ai.sports.common.entity.AiSportsConstant.ENTRY_EXPIRE_TIME;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("CourseStudentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements ICourseStudentService {

    @Autowired
    private IGroupStudentService groupStudentService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    @Override
    public List<Long> findByCourseId(Long courseId) {
        List<CourseStudent> courseStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId));
        if (CollectionUtils.isEmpty(courseStudentList)) {
            return new ArrayList<>();
        }
        return courseStudentList.stream().map(CourseStudent::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<User> findUserByCourseId(Long courseId) {
        return this.baseMapper.findUserByCourseId(courseId);
    }

    @Override
    public List<Long> findByUserId(Long userId) {
        List<CourseStudent> courseStudentList = this.baseMapper.selectList(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getUserId, userId));
        if (CollectionUtils.isEmpty(courseStudentList)) {
            return new ArrayList<>();
        }
        return courseStudentList.stream().map(CourseStudent::getCourseId).collect(Collectors.toList());
    }

    @Override
    public CourseStudent findByUserCourseId(Long userId, Long courseId) {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId));
    }

    @Override
    public List<CourseStudent> findByUserNoCourseId(Long userId, Long courseId) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<CourseStudent>().ne(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(Long userId, Long courseId) {
        // 将用户的小组关系查询出来，得出学生在哪个小组
        GroupStudent groupStudent = groupStudentService.findByCourseIdAndUserId(courseId, userId);
        // 再将学生关系删除掉
        groupStudentService.removeByGroupIdAndUserId(groupStudent.getGroupId(), groupStudent.getUserId());

        // TODO 需要更新其他学生的排序
        // 将列表中的学生删除掉
        removeEntryStudentList2Redis(courseId, userId);

        return this.baseMapper.delete(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId)) > 0;
    }

    @Override
    public Map<Long, Long> findCourseStudentCount() {
        List<CourseStudentCountDto> countDtos = this.baseMapper.findCourseStudentCount();

        if (CollectionUtils.isEmpty(countDtos)) {
            new HashMap<>(0);
        }
        return countDtos.stream().collect(Collectors.toMap(CourseStudentCountDto::getCourseId, CourseStudentCountDto::getCurrentCount, (e1, e2) -> e1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseEntryVo saveStudentAndGroup(CourseStudent courseStudent) throws AiSportsException {
        CourseEntryVo entryVo = new CourseEntryVo();

        // 查询一个课程中还可以加入的小组，小组的上限人数>组内人数的小组
        Group group = groupService.findCanJoinGroup(courseStudent.getCourseId());
        // 如果这里查不到的话，说明所有的小组已经报满了，不能再报课
        if (group == null) {
            // 在不满足报名条件时，删除报名记录
            removeEntryStudentList2Redis(courseStudent.getCourseId(), courseStudent.getUserId());
            throw new AiSportsException("今日当前课程已经报满，请明日再来！");
        }
        try {
            GroupStudent groupStudent = new GroupStudent();
            groupStudent.setCourseId(courseStudent.getCourseId());
            groupStudent.setGroupId(group.getGroupId());
            groupStudent.setUserId(courseStudent.getUserId());
            groupStudent.setCreateTime(new Date());
            // 保存小组和学生的关系
            groupStudentService.save(groupStudent);

            // 统计报名序号
//            Long sort = baseMapper.findSortByMax(courseStudent.getCourseId());
//            courseStudent.setSort(sort);
            // 保存报名信息
            this.save(courseStudent);

            entryVo.setGroupId(group.getGroupId());
            entryVo.setGroupName(group.getGroupName());
//            entryVo.setSort(sort);
        } catch (Exception e) {
            log.info("学生报课保存失败，courseId:{}, userId:{}, 发生异常：{}", courseStudent.getCourseId(), courseStudent.getUserId(), e.getMessage());
            e.printStackTrace();
            // 将学生列表删除
            removeEntryStudentList2Redis(courseStudent.getCourseId(), courseStudent.getUserId());
        }

        return entryVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeByCourseId(String courseId) {
        return this.baseMapper.delete(new LambdaQueryWrapper<CourseStudent>().eq(CourseStudent::getCourseId, courseId));
    }

    @Override
    public Long findCountByUserId(Long courseId) {
        return this.baseMapper.findCountByUserId(courseId);
    }

    @Override
    public Long setEntryStudentList2Redis(Long courseId, Long userId) {
        String key = getEntryStudentListKey(courseId);
        // 当前报名人数
        Long size = jedisPoolUtil.hLen(key) + 1;

        jedisPoolUtil.hSet(key, String.valueOf(userId), String.valueOf(size));
        // 先查询key的存活时间
        long ttl = jedisPoolUtil.ttl(key);
        if(ttl < 1L){
            // 设置过期时间
            jedisPoolUtil.expire(key, ENTRY_EXPIRE_TIME);
        }
        return size;
    }

    @Override
    public Integer findEntryStudentList2Redis(Long courseId, Long userId) {
        String value = jedisPoolUtil.hGet(getEntryStudentListKey(courseId), String.valueOf(userId));
        if(StringUtils.isBlank(value)){
            return null;
        }
        return Integer.valueOf(value);
    }

    @Override
    public void removeEntryStudentList2Redis(Long courseId, Long userId) {
        jedisPoolUtil.hDel(getEntryStudentListKey(courseId), String.valueOf(userId));
    }

    @Override
    public void removeEntryStudentList2Redis(Long courseId) {
        jedisPoolUtil.del(getEntryStudentListKey(courseId));
    }

    @Override
    public Long getLenEntryStudentList2Redis(Long courseId) {
        return jedisPoolUtil.hLen(getEntryStudentListKey(courseId));
    }

    private String getEntryStudentListKey(Long courseId){
        return "entry_student_list_" + courseId;
    }

}
