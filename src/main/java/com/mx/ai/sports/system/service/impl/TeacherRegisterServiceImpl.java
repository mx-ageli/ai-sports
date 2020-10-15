package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.TeacherRegister;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.mapper.TeacherRegisterMapper;
import com.mx.ai.sports.system.service.ITeacherRegisterService;
import com.mx.ai.sports.system.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TeacherRegisterServiceImpl extends ServiceImpl<TeacherRegisterMapper, TeacherRegister> implements ITeacherRegisterService {

    @Autowired
    private IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRegister(List<TeacherRegister> teacherRegisters) {
        // 先将老师注册表的数据先保存
        Boolean isSave = saveBatch(teacherRegisters);
        log.info("批量的保存的老师的注册数据！成功状态：{}，保存数量：{}", isSave, teacherRegisters.size());

        for (TeacherRegister tr : teacherRegisters) {
            User user = userService.checkUserAndRegister(tr.getUsername());
            log.info("老师初始化注册账号成功！userId：{}，手机号：{}，任课教师：{}", user.getUserId(), user.getUsername(), user.getFullName());
        }

        return true;
    }
}
