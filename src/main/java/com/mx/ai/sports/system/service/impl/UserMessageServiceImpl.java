package com.mx.ai.sports.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.system.entity.UserMessage;
import com.mx.ai.sports.system.mapper.UserMessageMapper;
import com.mx.ai.sports.system.service.IUserMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mengjiaxin
 * @date 2020/9/8 6:50 下午
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {


}
