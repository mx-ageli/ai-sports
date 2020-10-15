package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.TempStudent;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.vo.TempStudentVo;

import java.util.List;

/**
 * 学生临时信息
 *
 * @author Mengjiaxin
 * @date 2020/10/14 5:23 下午
 */
public interface ITempStudentService extends IService<TempStudent> {


    Boolean batchTempStudent(List<TempStudent> tempStudents);

    /**
     * 查询学生临时信息
     * @param fullName
     * @param sno
     * @return
     */
    TempStudentVo findTempStudentInfo(String fullName, String sno);

    /**
     * 绑定学生基础信息
     * @param tempStudent
     * @param user
     * @return
     */
    Boolean bind(TempStudent tempStudent, User user);
}
