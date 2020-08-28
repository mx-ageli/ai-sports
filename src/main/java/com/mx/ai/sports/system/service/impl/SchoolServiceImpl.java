package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.School;
import com.mx.ai.sports.system.mapper.SchoolMapper;
import com.mx.ai.sports.system.service.ISchoolService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学校service
 * @author Mengjiaxin
 * @date 2020/8/28 10:02 上午
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {



}
