package com.mx.ai.sports.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.vo.SubjectStudentVo;
import com.mx.ai.sports.system.vo.UserCountVo;
import com.mx.ai.sports.system.vo.UserSmallVo;
import com.mx.ai.sports.system.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户Mapper
 *
 * @author Mengjiaxin
 * @date 2019-08-20 19:46
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户详细
     *
     * @param userId
     * @return
     */
    UserVo findVoById(@Param("userId") Long userId);

    /**
     *
     * @param page
     * @param classesId
     * @return
     */
    IPage<UserSmallVo> findByClassesId(Page<User> page, @Param("classesId") Long classesId);

    /**
     *
     * @param schoolId
     * @return
     */
    List<User> findStudentBySchoolId(Long schoolId);

    /**
     *
     * @param userId
     * @return
     */
    SubjectStudentVo findSubjectByUserId(Long userId);

    /**
     * 每日用户活跃数量
     * @param startTime
     * @param endTime
     * @return
     */
    List<UserCountVo> findActiveUserCount(Date startTime, Date endTime);

    /**
     * 每日用户活跃数量
     * @param startTime
     * @param endTime
     * @return
     */
    List<UserCountVo> findAddUserCount(Date startTime, Date endTime);

}
