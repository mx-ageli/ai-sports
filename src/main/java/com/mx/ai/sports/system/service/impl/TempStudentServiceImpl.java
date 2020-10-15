package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.TempStudent;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.mapper.TempStudentMapper;
import com.mx.ai.sports.system.service.ITempStudentService;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.TempStudentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TempStudentServiceImpl extends ServiceImpl<TempStudentMapper, TempStudent> implements ITempStudentService {


    @Autowired
    private IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchTempStudent(List<TempStudent> tempStudents) {
        // 先将老的数据删除 根据学号
        // 所有导入学生的学号
        List<String> snoList = tempStudents.stream().map(TempStudent::getSno).distinct().collect(Collectors.toList());
        // 先根据这些学号将老数据先删除
        this.remove(new LambdaQueryWrapper<TempStudent>().in(TempStudent::getSno, snoList));
        // 再将新数据都批量保存
        return this.saveBatch(tempStudents);
    }

    @Override
    public TempStudentVo findTempStudentInfo(String fullName, String sno) {
        return this.baseMapper.findTempStudentInfo(fullName, sno);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bind(TempStudent tempStudent, User user) {
        user.setFullName(tempStudent.getFullName());
        user.setSex(tempStudent.getSex());
        user.setSno(tempStudent.getSno());
        user.setSchoolId(tempStudent.getSchoolId());
        user.setClassesId(tempStudent.getClassesId());
        user.setSubjectId(tempStudent.getSubjectId());
        user.setSubjectSeqId(tempStudent.getSubjectSeqId());
        user.setTeacherId(tempStudent.getTeacherId());

        // 将临时信息设置为已经注册
        tempStudent.setIsRegister(true);
        this.saveOrUpdate(tempStudent);

        return userService.saveOrUpdate(user);
    }
}
