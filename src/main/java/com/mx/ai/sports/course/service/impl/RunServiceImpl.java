package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunRule;
import com.mx.ai.sports.course.mapper.RunMapper;
import com.mx.ai.sports.course.mapper.RunRuleMapper;
import com.mx.ai.sports.course.service.IRunRuleService;
import com.mx.ai.sports.course.service.IRunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("RunService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RunServiceImpl extends ServiceImpl<RunMapper, Run> implements IRunService {


}
