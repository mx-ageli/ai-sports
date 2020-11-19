package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.exception.AiSportsException;
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
import wiki.xsx.core.util.RedisUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
//        GroupStudent groupStudent = groupStudentService.findByCourseIdAndUserId(courseId, userId);
        // 将学生关系删除掉
        groupStudentService.removeByUserId(userId);

        // TODO 需要更新其他学生的排序
        // 将列表中的学生删除掉
        // 报课数量中自减一
        minusCountEntryStudent2Redis(courseId);
        // 将学生移除
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
            // 先去数据库里面查询学生， 如果学生已经报名了，就不删除缓存。
            CourseStudent existCourseStudent = findByUserCourseId(courseStudent.getUserId(), courseStudent.getCourseId());
            if (existCourseStudent == null) {
                log.info("执行删除报课redis操作，courseId:{}, userId:{}", courseStudent.getCourseId(), courseStudent.getUserId());
                // 报课数量中自减一
                minusCountEntryStudent2Redis(courseStudent.getCourseId());
                // 将学生移除
                removeEntryStudentList2Redis(courseStudent.getCourseId(), courseStudent.getUserId());
            }
            throw new AiSportsException("今日当前课程已经报满，请明日再来！");
        }
        try {
            // 先保存报名信息，在往小组里面加
            this.save(courseStudent);

            GroupStudent groupStudent = new GroupStudent();
            groupStudent.setCourseId(courseStudent.getCourseId());
            groupStudent.setGroupId(group.getGroupId());
            groupStudent.setUserId(courseStudent.getUserId());
            groupStudent.setCreateTime(new Date());
            // 保存小组和学生的关系
            groupStudentService.save(groupStudent);

            entryVo.setGroupId(group.getGroupId());
            entryVo.setGroupName(group.getGroupName());
        } catch (Exception e) {
            // 先去数据库里面查询学生， 如果学生已经报名了，就不删除缓存。
            CourseStudent existCourseStudent = findByUserCourseId(courseStudent.getUserId(), courseStudent.getCourseId());
            if (existCourseStudent == null) {
                log.info("执行删除报课redis操作，courseId:{}, userId:{}", courseStudent.getCourseId(), courseStudent.getUserId());
                // 报课数量中自减一
                minusCountEntryStudent2Redis(courseStudent.getCourseId());
                // 将学生移除
                removeEntryStudentList2Redis(courseStudent.getCourseId(), courseStudent.getUserId());
            }
            log.info("学生报课保存失败，courseId:{}, userId:{}, 发生异常：{}", courseStudent.getCourseId(), courseStudent.getUserId(), e.getMessage());
            e.printStackTrace();
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
        String key = getHashEntryStudentKey(courseId);

        // 给课程往redis中计数加1
        Long entryCountRedis = plusCountEntryStudent2Redis(courseId);
        // 使用计数器中的数量
        RedisUtil.getHashHandler().putAsObj(key, String.valueOf(userId), entryCountRedis);

        if(entryCountRedis <= 1L){
            RedisUtil.getKeyHandler().expire(key, ENTRY_EXPIRE_TIME, TimeUnit.SECONDS);
            // 给计数器的key也设置过期时间
            RedisUtil.getKeyHandler().expire(getCountEntryStudentKey(courseId), ENTRY_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return entryCountRedis;
    }

    @Override
    public Long findEntryStudentList2Redis(Long courseId, Long userId) {
        String key = getHashEntryStudentKey(courseId);

        return RedisUtil.getHashHandler().getAsObj(key, String.valueOf(userId));
    }

    @Override
    public void removeEntryStudentList2Redis(Long courseId, Long userId) {
        String key = getHashEntryStudentKey(courseId);

        RedisUtil.getHashHandler().remove(key, String.valueOf(userId));
    }

    @Override
    public void removeEntryStudentList2Redis(Long courseId) {
        String key = getHashEntryStudentKey(courseId);

        RedisUtil.getKeyHandler().remove(key);
    }

    @Override
    public Long getLenEntryStudentList2Redis(Long courseId) {
        String key = getHashEntryStudentKey(courseId);
        return RedisUtil.getHashHandler().size(key);
    }



    @Override
    public void removeCountEntryStudent2Redis(Long courseId) {
        String key = getCountEntryStudentKey(courseId);

        RedisUtil.getKeyHandler().remove(key);
    }

    @Override
    public Long minusCountEntryStudent2Redis(Long courseId) {
        String key = getCountEntryStudentKey(courseId);

        return RedisUtil.getNumberHandler().decrementLong(key);
    }

    @Override
    public Long plusCountEntryStudent2Redis(Long courseId) {
        String key = getCountEntryStudentKey(courseId);

        return RedisUtil.getNumberHandler().incrementLong(key);
    }

    @Override
    public Long getCountEntryStudent2Redis(Long courseId) {
        String key = getCountEntryStudentKey(courseId);

        return RedisUtil.getNumberHandler().getLong(key);
    }

    private String getHashEntryStudentKey(Long courseId) {
        return "hash_entry_student_" + courseId;
    }

    private String getCountEntryStudentKey(Long courseId){
        return "count_entry_student_" + courseId;
    }

}
