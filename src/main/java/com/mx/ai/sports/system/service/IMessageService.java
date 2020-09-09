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


    /**
     * 查询我的消息
     *
     * @param query
     * @param currentUserId
     * @return
     */
    IPage<MessageVo> findMyMessage(QueryRequest query, Long currentUserId);

    /**
     * 保存系统消息
     *
     * @param message
     * @param userIds
     * @return
     */
    Boolean saveSendMessage(Message message, List<Long> userIds);

    /**
     * 课程发布后推送的消息
     *
     * @param userId
     * @param course
     * @return
     * @throws AiSportsException
     */
    Boolean courseAddPush(Long userId, Course course) throws AiSportsException;

    /**
     * 课程开始前的消息推送
     *
     * @param course
     * @return
     * @throws AiSportsException
     */
    Boolean courseStartPush(Course course) throws AiSportsException;


}
