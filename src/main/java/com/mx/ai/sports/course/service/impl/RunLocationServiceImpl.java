package com.mx.ai.sports.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.course.entity.RunLocation;
import com.mx.ai.sports.course.mapper.RunLocationMapper;
import com.mx.ai.sports.course.service.IRunLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mengjiaxin
 * @date 2020/8/17 7:18 下午
 */
@Slf4j
@Service("RunLocationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RunLocationServiceImpl extends ServiceImpl<RunLocationMapper, RunLocation> implements IRunLocationService {


}
