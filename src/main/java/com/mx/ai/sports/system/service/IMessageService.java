package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.system.entity.Message;
import com.mx.ai.sports.system.vo.MessageVo;

import java.util.List;

/**
 * 消息service
 *
 * @author Mengjiaxin
 * @date 2020/9/8 6:27 下午
 */
public interface IMessageService extends IService<Message> {


    IPage<MessageVo> findMyMessage(QueryRequest query, Long currentUserId);

    Boolean saveSendMessage(Message message, List<Long> userIds);

    Boolean courseAddPush(Long userId, Course course) throws AiSportsException;

}
