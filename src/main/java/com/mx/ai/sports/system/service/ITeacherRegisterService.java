package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.TeacherRegister;

import java.util.List;

/**
 * 老师注册
 * @author Mengjiaxin
 * @date 2020/10/14 4:34 下午
 */

public interface ITeacherRegisterService extends IService<TeacherRegister> {

    /**
     * 批量的注册老师账号
     * @param teacherRegisters
     * @return
     */
    boolean batchRegister(List<TeacherRegister> teacherRegisters);

}
